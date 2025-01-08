package com.bumble.appyx.core.node

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.AppyxTestScenario
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.transition.localMovableContentWithTargetVisibility
import com.bumble.appyx.core.node.BackStackMovableContentTest.NavTarget.NavTarget1
import com.bumble.appyx.core.node.BackStackMovableContentTest.NavTarget.NavTarget2
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class BackStackMovableContentTest {

    private var currentCounter: Int = Int.MIN_VALUE

    private val backStack = BackStack<NavTarget>(
        savedStateMap = null,
        initialElement = NavTarget1
    )

    var nodeFactory: (buildContext: BuildContext) -> TestParentNode = {
        TestParentNode(
            buildContext = it,
            backStack = backStack
        )
    }

    @get:Rule
    val rule = AppyxTestScenario { buildContext ->
        nodeFactory(buildContext)
    }

    @Test
    fun `GIVEN_backStack_with_movable_content_WHEN_push_THEN_persits_movable_content_state`() {
        rule.start()

        val counterOne = currentCounter
        backStack.push(NavTarget2)
        rule.mainClock.advanceTimeBy(COUNTER_DELAY)

        assertEquals(counterOne + 1, currentCounter)

        val counterTwo = currentCounter
        backStack.pop()
        rule.mainClock.advanceTimeBy(COUNTER_DELAY)

        assertEquals(counterTwo + 1, currentCounter)
    }

    @Parcelize
    sealed class NavTarget : Parcelable {

        data object NavTarget1 : NavTarget()

        data object NavTarget2 : NavTarget()
    }

    inner class TestParentNode(
        buildContext: BuildContext,
        val backStack: BackStack<NavTarget>
    ) : ParentNode<NavTarget>(
        buildContext = buildContext,
        navModel = backStack
    ) {

        override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
            when (navTarget) {
                NavTarget1 -> node(buildContext) {
                    MovableContent()
                }

                NavTarget2 -> node(buildContext) {
                    MovableContent()
                }
            }

        @Composable
        override fun View(modifier: Modifier) {
            Children(
                navModel = navModel,
                withSharedElementTransition = true,
                withMovableContent = true
            )
        }

        @Composable
        fun MovableContent() {
            localMovableContentWithTargetVisibility(key = "key") {
                var counter by remember {
                    mutableIntStateOf(Random.nextInt(0, 10000).apply {
                        currentCounter = this
                    })
                }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(COUNTER_DELAY)
                        counter++
                        currentCounter = counter
                    }
                }
            }?.invoke()
        }
    }
}

private val COUNTER_DELAY = 1000L
