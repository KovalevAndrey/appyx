package com.bumble.appyx.components.spotlight.ui.sliderscale

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.spotlight.SpotlightModel.State
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.CREATED
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.DESTROYED
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.SELECTED
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.STANDARD
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.DISMISSED
import com.bumble.appyx.components.spotlight.operation.Dismiss
import com.bumble.appyx.components.spotlight.operation.Next
import com.bumble.appyx.components.spotlight.operation.Previous
import com.bumble.appyx.components.spotlight.operation.Select
import com.bumble.appyx.components.spotlight.operation.Deselect
import com.bumble.appyx.interactions.AppyxLogger
import com.bumble.appyx.interactions.core.ui.context.TransitionBounds
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.Drag
import com.bumble.appyx.interactions.core.ui.gesture.Gesture
import com.bumble.appyx.interactions.core.ui.gesture.GestureFactory
import com.bumble.appyx.interactions.core.ui.gesture.dragDirection4
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.interactions.core.ui.property.impl.GenericFloatProperty
import com.bumble.appyx.interactions.core.ui.property.impl.GenericFloatProperty.Target
import com.bumble.appyx.interactions.core.ui.property.impl.Height
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.property.impl.RotationY
import com.bumble.appyx.interactions.core.ui.property.impl.RoundedCorners
import com.bumble.appyx.interactions.core.ui.property.impl.Width
import com.bumble.appyx.interactions.core.ui.property.impl.ZIndex
import com.bumble.appyx.interactions.core.ui.state.MatchedTargetUiState
import com.bumble.appyx.transitionmodel.BaseMotionController
import kotlinx.coroutines.flow.MutableStateFlow

class SpotlightSliderScale<InteractionTarget : Any>(
    uiContext: UiContext,
    private val orientation: Orientation = Orientation.Horizontal, // TODO support RTL
) : BaseMotionController<InteractionTarget, State<InteractionTarget>, MutableUiState, TargetUiState>(
    uiContext = uiContext
) {
    private val uiStateToPositionMap: MutableMap<MutableUiState, MutableStateFlow<Int>> = mutableMapOf()
    private val width: Dp = uiContext.transitionBounds.widthDp
    private val height: Dp = uiContext.transitionBounds.heightDp
    private val scrollX = GenericFloatProperty(
        uiContext,
        Target(0f)
    ) // TODO sync this with the model's initial value rather than assuming 0
    override val viewpointDimensions: List<Pair<(State<InteractionTarget>) -> Float, GenericFloatProperty>> =
        listOf(
            { state: State<InteractionTarget> -> state.activeIndex } to scrollX
        )

    private val created: TargetUiState = TargetUiState(
        position = Position.Target(DpOffset(0.dp, width)),
        alpha = Alpha.Target(1f),
        width = Width.Target(0f),
        height = Height.Target(0f),
        zIndex = ZIndex.Target(1f),
        rotationY = RotationY.Target(0f),
        roundedCorners = RoundedCorners.Target(0),
    )

    private val standard: TargetUiState = TargetUiState(
        position = Position.Target(DpOffset.Zero),
        alpha = Alpha.Target(1f),
        width = Width.Target(0.65f),
        height = Height.Target(0.4f),
        zIndex = ZIndex.Target(1f),
        rotationY = RotationY.Target(0f),
        roundedCorners = RoundedCorners.Target(0),
    )

    private val selected: TargetUiState = TargetUiState(
        position = Position.Target(DpOffset.Zero),
        width = Width.Target(1f),
        alpha = Alpha.Target(1f),
        rotationY = RotationY.Target(0f),
        height = Height.Target(1f),
        zIndex = ZIndex.Target(2f),
        roundedCorners = RoundedCorners.Target(0),
    )

    private val swipedDown: TargetUiState = TargetUiState(
        position = Position.Target(DpOffset(0.dp, y = height)),
        alpha = Alpha.Target(0f, easing = CubicBezierEasing(0.1f, 0.0f, 0.2f, 1.0f)),
        width = Width.Target(0.65f),
        height = Height.Target(0.4f),
        rotationY = RotationY.Target(0f),
        zIndex = ZIndex.Target(2f),
        roundedCorners = RoundedCorners.Target(0),
    )

    private val destroyed: TargetUiState = TargetUiState(
        position = Position.Target(DpOffset(x = 0.dp, y = -height)),
        width = Width.Target(0f),
        rotationY = RotationY.Target(0f),
        alpha = Alpha.Target(1f, easing = FastOutSlowInEasing),
        height = Height.Target(0f),
        zIndex = ZIndex.Target(1f),
        roundedCorners = RoundedCorners.Target(0),
    )

    override fun updatingState(mutableUiState: MutableUiState, position: Int) {
        val flow = uiStateToPositionMap[mutableUiState] ?: return
        flow.value = position

        AppyxLogger.d("STATE", "$mutableUiState updated position: $position")
    }

    override fun State<InteractionTarget>.toUiTargets(): List<MatchedTargetUiState<InteractionTarget, TargetUiState>> {
        return positions.flatMapIndexed { index, position ->
            position.elements.map {
                MatchedTargetUiState(
                    element = it.key,
                    targetUiState = TargetUiState(
                        base = when (it.value) {
                            CREATED -> created
                            STANDARD -> standard
                            DESTROYED -> destroyed
                            SELECTED -> selected
                            DISMISSED -> swipedDown
                        },
                        positionInList = MutableStateFlow(index),
                        elementWidth = width,
                        zIndex = ZIndex.Target(1f),
                        roundedCorners = when (it.value) {
                            SELECTED -> RoundedCorners.Target(0)
                            else -> RoundedCorners.Target(5)
                        }
                    )
                )
            }
        }
    }

    class Gestures<InteractionTarget>(
        transitionBounds: TransitionBounds,
        private val orientation: Orientation = Orientation.Horizontal,
        private val reverseOrientation: Boolean = false,
    ) : GestureFactory<InteractionTarget, State<InteractionTarget>> {
        private val width = transitionBounds.widthPx.toFloat()
        private val height = transitionBounds.heightPx.toFloat()

        override fun createGesture(
            state: State<InteractionTarget>,
            delta: Offset,
            density: Density
        ): Gesture<InteractionTarget, State<InteractionTarget>> =
            when (dragDirection4(delta)) {
                Drag.Direction4.LEFT -> if (state.positions[state.activeIndex.toInt()].elements.any { it.value == STANDARD }) {
                    Gesture(
                        operation = if (reverseOrientation) Previous() else Next(),
                        completeAt = Offset(-width, 0f)
                    )
                } else {
                    Gesture.Noop()
                }

                Drag.Direction4.RIGHT ->
                    if (state.positions[state.activeIndex.toInt()].elements.any { it.value == STANDARD }) {
                        Gesture(
                            operation = if (reverseOrientation) Next() else Previous(),
                            completeAt = Offset(width, 0f)
                        )
                    } else {
                        Gesture.Noop()
                    }

                Drag.Direction4.UP -> Gesture(
                    operation = Select(),
                    completeAt = Offset(0f, -height / 4),
                    isContinuous = false
                )

                Drag.Direction4.DOWN -> if (state.positions[state.activeIndex.toInt()].elements.any { it.value == SELECTED }) {
                    Gesture(
                        operation = Deselect(),
                        completeAt = Offset(0f, height / 4),
                        isContinuous = false
                    )

                } else if (state.positions[state.activeIndex.toInt()].elements.any { it.value == STANDARD }) {
                    Gesture(
                        operation = Dismiss(),
                        completeAt = Offset(0f, height / 2),
                        isContinuous = false
                    )
                } else {
                    Gesture.Noop()
                }
            }
    }

    override fun mutableUiStateFor(
        uiContext: UiContext,
        targetUiState: TargetUiState, position: Int,
    ): MutableUiState {
        val positionInList: MutableStateFlow<Int> = MutableStateFlow(position)
        val value =
            targetUiState.toMutableState(uiContext, scrollX.renderValueFlow, positionInList, width)
        uiStateToPositionMap[value] = positionInList
        return value
    }
}

