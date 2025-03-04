package com.bumble.appyx.core.composable

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.navigation.removedElementKeys
import com.bumble.appyx.core.navigation.transition.JumpToEndTransitionHandler
import com.bumble.appyx.core.navigation.transition.TransitionBounds
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionHandler
import com.bumble.appyx.core.navigation.transition.TransitionParams
import com.bumble.appyx.core.node.LocalMovableContentMap
import com.bumble.appyx.core.node.LocalNodeTargetVisibility
import com.bumble.appyx.core.node.LocalSharedElementScope
import com.bumble.appyx.core.node.ParentNode
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
inline fun <reified NavTarget : Any, State> ParentNode<NavTarget>.Children(
    navModel: NavModel<NavTarget, State>,
    modifier: Modifier = Modifier,
    transitionHandler: TransitionHandler<NavTarget, State> = remember { JumpToEndTransitionHandler() },
    withSharedElementTransition: Boolean = false,
    withMovableContent: Boolean = false,
    noinline block: @Composable ChildrenTransitionScope<NavTarget, State>.() -> Unit = {
        children<NavTarget> { child ->
            child()
        }
    }
) {
    val density = LocalDensity.current.density
    var transitionBounds by remember { mutableStateOf(IntSize(0, 0)) }
    val transitionParams by remember(transitionBounds) {
        derivedStateOf {
            TransitionParams(
                bounds = TransitionBounds(
                    width = Dp(transitionBounds.width / density),
                    height = Dp(transitionBounds.height / density)
                )
            )
        }
    }
    if (withSharedElementTransition) {
        SharedTransitionLayout(modifier = modifier
            .onSizeChanged {
                transitionBounds = it
            }
        ) {
            CompositionLocalProvider(
                /** LocalSharedElementScope will be consumed by children UI to apply shareElement modifier */
                LocalSharedElementScope provides this,
                LocalMovableContentMap provides if (withMovableContent) mutableMapOf() else null
            ) {
                block(
                    ChildrenTransitionScope(
                        transitionHandler = transitionHandler,
                        transitionParams = transitionParams,
                        navModel = navModel
                    )
                )
            }
        }
    } else {
        Box(modifier = modifier
            .onSizeChanged {
                transitionBounds = it
            }
        ) {
            CompositionLocalProvider(
                /** If sharedElement is not supported for this Node - provide null otherwise children
                 * can consume ascendant's LocalSharedElementScope */
                LocalSharedElementScope provides null,
                LocalMovableContentMap provides if (withMovableContent) mutableMapOf() else null
            ) {
                block(
                    ChildrenTransitionScope(
                        transitionHandler = transitionHandler,
                        transitionParams = transitionParams,
                        navModel = navModel
                    )
                )
            }
        }
    }
}

@Immutable
class ChildrenTransitionScope<T : Any, S>(
    private val transitionHandler: TransitionHandler<T, S>,
    private val transitionParams: TransitionParams,
    private val navModel: NavModel<T, S>
) {

    @Composable
    inline fun <reified V : T> ParentNode<T>.children(
        noinline block: @Composable ChildTransitionScope<S>.(
            child: ChildRenderer,
            transitionDescriptor: TransitionDescriptor<T, S>
        ) -> Unit,
    ) {
        children(V::class, block)
    }

    @Composable
    inline fun <reified V : T> ParentNode<T>.children(
        noinline block: @Composable ChildTransitionScope<S>.(child: ChildRenderer) -> Unit,
    ) {
        children(V::class, block)
    }

    @Composable
    @SuppressLint("ComposableNaming")
    fun ParentNode<T>.children(
        clazz: KClass<out T>,
        block: @Composable ChildTransitionScope<S>.(ChildRenderer) -> Unit,
    ) {
        _children(clazz) { scope, child, _ ->
            scope.block(child)
        }
    }

    @Composable
    @SuppressLint("ComposableNaming")
    fun ParentNode<T>.children(
        clazz: KClass<out T>,
        block: @Composable ChildTransitionScope<S>.(
            ChildRenderer,
            TransitionDescriptor<T, S>
        ) -> Unit,
    ) {
        _children(clazz) { scope, child, descriptor ->
            scope.block(
                child,
                descriptor,
            )
        }
    }

    @SuppressLint("ComposableNaming")
    @Composable
    private fun ParentNode<T>._children(
        clazz: KClass<out T>,
        block: @Composable (
            transitionScope: ChildTransitionScope<S>,
            child: ChildRenderer,
            transitionDescriptor: TransitionDescriptor<T, S>
        ) -> Unit
    ) {
        val saveableStateHolder = rememberSaveableStateHolder()

        LaunchedEffect(navModel) {
            navModel
                .removedElementKeys()
                .map { list ->
                    list.filter { clazz.isInstance(it.navTarget) }
                }
                .collect { deletedKeys ->
                    deletedKeys.forEach { navKey ->
                        saveableStateHolder.removeState(navKey)
                    }
                }
        }

        val screenStateFlow = remember {
            this@ChildrenTransitionScope
                .navModel
                .screenState
        }

        val children by screenStateFlow.collectAsState()

        children
            .onScreen
            .filter { clazz.isInstance(it.key.navTarget) }
            .forEach { navElement ->
                key(navElement.key.id) {
                    CompositionLocalProvider(
                        LocalNodeTargetVisibility provides
                                children.onScreenWithVisibleTargetState.contains(navElement)
                    ) {
                        Child(
                            navElement,
                            saveableStateHolder,
                            transitionParams,
                            transitionHandler,
                            block
                        )
                    }
                }
            }
    }
}
