package com.bumble.appyx.navigation.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.round
import com.bumble.appyx.interactions.core.defaultExtraTouch

import com.bumble.appyx.interactions.core.gesture.GestureValidator
import com.bumble.appyx.interactions.core.gesture.detectDragGesturesOrCancellation
import com.bumble.appyx.interactions.core.model.BaseAppyxComponent
import com.bumble.appyx.interactions.core.model.removedElements
import com.bumble.appyx.interactions.core.ui.context.TransitionBounds
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.helper.gestureModifier
import com.bumble.appyx.interactions.core.ui.output.ElementUiModel
import com.bumble.appyx.navigation.node.ParentNode
import kotlin.math.roundToInt

@Composable
inline fun <reified InteractionTarget : Any, ModelState : Any> ParentNode<InteractionTarget>.AppyxComponent(
    appyxComponent: BaseAppyxComponent<InteractionTarget, ModelState>,
    modifier: Modifier = Modifier,
    clipToBounds: Boolean = false,
    gestureValidator: GestureValidator = GestureValidator.defaultValidator,
    gestureExtraTouchArea: Dp = defaultExtraTouch,
    noinline block: @Composable ChildrenTransitionScope<InteractionTarget, ModelState>.() -> Unit = {
        children { child, elementUiModel ->
            child(
                modifier = Modifier.gestureModifier(
                    appyxComponent = appyxComponent,
                    key = elementUiModel.element,
                )
            )
        }
    }
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val screenWidthPx = (LocalConfiguration.current.screenWidthDp * density.density).roundToInt()
    val screenHeightPx = (LocalConfiguration.current.screenHeightDp * density.density).roundToInt()
    var uiContext by remember { mutableStateOf<UiContext?>(null) }

    LaunchedEffect(uiContext) {
        uiContext?.let { appyxComponent.updateContext(it) }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .then(if (clipToBounds) Modifier.clipToBounds() else Modifier)
            .onPlaced {
                uiContext = UiContext(
                    coroutineScope = coroutineScope,
                    transitionBounds = TransitionBounds(
                        density = density,
                        widthPx = it.size.width,
                        heightPx = it.size.height,
                        screenWidthPx = screenWidthPx,
                        screenHeightPx = screenHeightPx
                    ),
                    clipToBounds = clipToBounds
                )
            }
    ) {
        block(
            ChildrenTransitionScope(
                appyxComponent = appyxComponent,
                gestureValidator = gestureValidator,
                gestureExtraTouchArea = gestureExtraTouchArea
            )
        )
    }

}

class ChildrenTransitionScope<InteractionTarget : Any, NavState : Any>(
    private val appyxComponent: BaseAppyxComponent<InteractionTarget, NavState>,
    private val gestureValidator: GestureValidator = GestureValidator.defaultValidator,
    private val gestureExtraTouchArea: Dp = defaultExtraTouch,
) {

    @SuppressLint("ComposableNaming")
    @Composable
    fun ParentNode<InteractionTarget>.children(
        block: @Composable (child: ChildRenderer, elementUiModel: ElementUiModel<InteractionTarget>) -> Unit
    ) {

        val saveableStateHolder = rememberSaveableStateHolder()

        LaunchedEffect(this@ChildrenTransitionScope.appyxComponent) {
            this@ChildrenTransitionScope.appyxComponent
                .removedElements()
                .collect { deletedKeys ->
                    deletedKeys.forEach { navKey ->
                        saveableStateHolder.removeState(navKey)
                    }
                }
        }


        val uiModels by this@ChildrenTransitionScope.appyxComponent.uiModels.collectAsState()

        val density = LocalDensity.current
        val gestureExtraTouchAreaPx = with(density) { gestureExtraTouchArea.toPx() }

        uiModels
            .forEach { elementUiModel ->
                key(elementUiModel.element.id) {
                    var transformedBoundingBox by remember(elementUiModel.element.id) {
                        mutableStateOf(
                            Rect.Zero
                        )
                    }
                    var offsetCenter by remember(elementUiModel.element.id) { mutableStateOf(Offset.Zero) }
                    val isVisible by elementUiModel.visibleState.collectAsState()
                    elementUiModel.persistentContainer()
                    if (isVisible) {
                        Child(
                            saveableStateHolder = saveableStateHolder,
                            decorator = block,
                            elementUiModel = elementUiModel.copy(
                                modifier = Modifier
                                    .offset { offsetCenter.round() }
                                    .pointerInput(appyxComponent) {
                                        detectDragGesturesOrCancellation(
                                            onDragStart = { position ->
                                                this@ChildrenTransitionScope.appyxComponent.onStartDrag(position)
                                            },
                                            onDrag = { change, dragAmount ->
                                                if (gestureValidator.isGestureValid(
                                                        change.position,
                                                        transformedBoundingBox.translate(-offsetCenter)
                                                    )
                                                ) {
                                                    change.consume()
                                                    this@ChildrenTransitionScope.appyxComponent.onDrag(dragAmount, density)
                                                    true
                                                } else {
                                                    this@ChildrenTransitionScope.appyxComponent.onDragEnd()
                                                    false
                                                }
                                            },
                                            onDragEnd = {
                                                this@ChildrenTransitionScope.appyxComponent.onDragEnd()
                                            },
                                        )
                                    }
                                    .offset { -offsetCenter.round() }
                                    .then(elementUiModel.modifier)
                                    .onPlaced {
                                        val localCenter = Offset(
                                            it.size.width.toFloat(),
                                            it.size.height.toFloat()
                                        ) / 2f
                                        transformedBoundingBox =
                                            it.boundsInParent().inflate(gestureExtraTouchAreaPx)
                                        offsetCenter = transformedBoundingBox.center - localCenter
                                    }
                            )
                        )
                    }
                }
            }

//        uiModels
//            .forEach { uiModel ->
//                key(uiModel.element.id) {
//                    uiModel.persistentContainer()
//                    val isVisible by uiModel.visibleState.collectAsState()
//                    if (isVisible) {
//                        Child(
//                            uiModel,
//                            saveableStateHolder,
//                            block
//                        )
//                    }
//                }
//            }
    }
}
