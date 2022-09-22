package com.bumble.appyx.navmodel.cards.transitionhandler

import android.annotation.SuppressLint
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumble.appyx.core.navigation.transition.ModifierTransitionHandler
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Bottom
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.IndicateLike
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.IndicatePass
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Queued
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Top
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.VoteLike
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.VotePass
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Suppress("TransitionPropertiesLabel")
class CardsTransitionHandler<T>(
    private val padding: Dp,
    private val transitionSpec: TransitionSpec<Cards.TransitionState, Float> = { tween(500) }
) : ModifierTransitionHandler<T, Cards.TransitionState>() {

    data class TargetStateValues(
        val dpOffset: DpOffset,
        val scale: Float,
        val angleDegrees: Float,
        val effectiveRadiusRatio: Float,
        val rotationY: Float,
        val rotationZ: Float,
        val zIndex: Float,
    )

    private val queued = TargetStateValues(
        dpOffset = DpOffset(0.dp, 0.dp),
        scale = 0f,
        angleDegrees = 0f,
        effectiveRadiusRatio = 1f,
        rotationY = 0f,
        rotationZ = 0f,
        zIndex = -1f,
    )

    private val bottom = queued.copy(
        scale = 0.85f,
        zIndex = 0f,
    )

    private val top = bottom.copy(
        scale = 1f,
        zIndex = 1f,
    )

    private val indicateLike = top.copy(
        rotationZ = 10f,
        angleDegrees = 10f,
        zIndex = 2f,
    )

    private val indicatePass = top.copy(
        rotationZ = -10f,
        angleDegrees = -10f,
        zIndex = 2f,
    )

    private val votePass = top.copy(
        dpOffset = DpOffset(-(1500).dp, 0.dp),
        rotationZ = -270f,
        zIndex = 2f,
    )

    private val voteLike = top.copy(
        dpOffset = DpOffset((1500).dp, 0.dp),
        rotationZ = 270f,
        zIndex = 2f,
    )

    @SuppressLint("UnusedTransitionTargetStateParameter", "ModifierFactoryExtensionFunction")
    override fun createModifier(
        modifier: Modifier,
        transition: Transition<Cards.TransitionState>,
        descriptor: TransitionDescriptor<T, Cards.TransitionState>
    ): Modifier = modifier.composed {

        val halfWidthDp = (descriptor.params.bounds.width.value) / 2
        val halfHeightDp = (descriptor.params.bounds.height.value) / 2
//        val radiusDp = min(halfWidthDp, halfHeightDp) // * 0.75f
//        val halfWidthDp = 0
//        val halfHeightDp = 0
        val oneAndHalfHeightDp = halfHeightDp * 3f
        val radiusDp = oneAndHalfHeightDp // * 0.75f

        val angleDegrees = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().angleDegrees }
        )

        val effectiveRadiusDp = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().effectiveRadiusRatio * radiusDp }
        )

        val arcOffsetDp = derivedStateOf {
            val angleRadians = Math.toRadians(angleDegrees.value.toDouble() - 90)
            val x = effectiveRadiusDp.value * cos(angleRadians)
            val y = effectiveRadiusDp.value * sin(angleRadians)
            Offset(x.toFloat(), y.toFloat())
        }

        val rotationY = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().rotationY })

        val rotationZ = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().rotationZ })

        val scale = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().scale })

        val dpOffsetX = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().dpOffset.x.value })

        val dpOffsetY = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().dpOffset.y.value })

        val zIndex = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = { it.target().zIndex })

        return@composed this
            .offset {
                IntOffset(
//                    x = (this.density * (halfWidthDp + dpOffsetX.value + arcOffsetDp.value.x)).roundToInt(),
//                    y = (this.density * (oneAndHalfHeightDp + dpOffsetY.value + arcOffsetDp.value.y)).roundToInt()
                    x = (this.density * (0 + dpOffsetX.value + arcOffsetDp.value.x)).roundToInt(),
                    y = (this.density * (oneAndHalfHeightDp + dpOffsetY.value + arcOffsetDp.value.y)).roundToInt()
                )
            }
            .zIndex(zIndex.value)
            .graphicsLayer(
                rotationY = rotationY.value,
                rotationZ = rotationZ.value
            )
            .scale(scale.value)
    }

    @Composable
    private fun Cards.TransitionState.target() =
        when (this) {
            is Queued -> queued
            is Bottom -> bottom
            is Top -> top
            is IndicateLike -> indicateLike
            is IndicatePass -> indicatePass
            is VoteLike -> voteLike
            is VotePass -> votePass
        }
}

@Composable
fun <T> rememberCardsTransitionHandler(
    childSize: Dp,
    transitionSpec: TransitionSpec<Cards.TransitionState, Float> = { tween(500) }
): ModifierTransitionHandler<T, Cards.TransitionState> = remember {
    CardsTransitionHandler(childSize, transitionSpec)
}
