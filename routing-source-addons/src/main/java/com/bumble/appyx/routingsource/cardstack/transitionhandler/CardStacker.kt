package com.bumble.appyx.routingsource.cardstack.transitionhandler

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import com.bumble.appyx.core.routing.transition.ModifierTransitionHandler
import com.bumble.appyx.core.routing.transition.TransitionDescriptor
import com.bumble.appyx.core.routing.transition.TransitionSpec
import com.bumble.appyx.routingsource.cardstack.CardStack
import kotlin.math.roundToInt

@Suppress("TransitionPropertiesLabel")
class CardStacker<Routing>(
    private val maxVisibleStashedItems: Int = 4,
    override val clipToBounds: Boolean = false,
    private val transitionSpec: TransitionSpec<CardStack.TransitionState, Offset> = {
        spring(stiffness = Spring.StiffnessVeryLow)
    },
) : ModifierTransitionHandler<Routing, CardStack.TransitionState>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<CardStack.TransitionState>,
        descriptor: TransitionDescriptor<Routing, CardStack.TransitionState>
    ): Modifier = modifier.composed {
        val width = descriptor.params.bounds.width.value
        val offset = transition.animateOffset(
            transitionSpec = transitionSpec,
        ) {
            when (it) {
                is CardStack.TransitionState.Created -> toOutsideRight(width)
                is CardStack.TransitionState.Active -> toCenter()
                is CardStack.TransitionState.Stashed -> stackCard(it.stashDepth + 1)
                is CardStack.TransitionState.Destroyed -> toOutsideRight(width)
            }
        }

        offset {
            IntOffset(
                x = (offset.value.x * this.density).roundToInt(),
                y = (offset.value.y * this.density).roundToInt()
            )
        }
    }

    private fun toOutsideRight(width: Float) = Offset(1.0f * width, 0f)

    private fun stackCard(stackCount: Int): Offset =
//        if (stackCount < 2) {
//            Offset(0f * stackCount, 0f)
//        } else {
            Offset(-24f * stackCount, 0f)
//        }

    private fun toCenter() = Offset(0f, 0f)
}

@Composable
fun <T> rememberCardStacker(
    transitionSpec: TransitionSpec<CardStack.TransitionState, Offset> = { spring(stiffness = Spring.StiffnessVeryLow) },
    clipToBounds: Boolean = false,
): ModifierTransitionHandler<T, CardStack.TransitionState> = remember {
    CardStacker(transitionSpec = transitionSpec, clipToBounds = clipToBounds)
}
