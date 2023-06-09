package com.bumble.appyx.components.spotlight.operation

import androidx.compose.animation.core.AnimationSpec
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.SpotlightModel.State.ElementState.STANDARD
import com.bumble.appyx.interactions.Parcelize
import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.transition.BaseOperation
import com.bumble.appyx.interactions.core.model.transition.Operation

@Parcelize
class Deselect<InteractionTarget>(
    override var mode: Operation.Mode = Operation.Mode.KEYFRAME
) : BaseOperation<SpotlightModel.State<InteractionTarget>>() {

    override fun isApplicable(state: SpotlightModel.State<InteractionTarget>): Boolean =
        state.positions[state.activeIndex.toInt()].elements.any { it.value == SpotlightModel.State.ElementState.SELECTED }

    override fun createFromState(baseLineState: SpotlightModel.State<InteractionTarget>): SpotlightModel.State<InteractionTarget> =
        baseLineState

    override fun createTargetState(fromState: SpotlightModel.State<InteractionTarget>): SpotlightModel.State<InteractionTarget> =
        fromState.copy(
            positions = fromState.positions.mapIndexed { index, position ->
                if (index.toFloat() == fromState.activeIndex) {
                    val elements = position.elements.keys
                    val map =
                        mutableMapOf<Element<InteractionTarget>, SpotlightModel.State.ElementState>()
                    elements.forEach {
                        map.put(it, STANDARD)
                    }
                    position.copy(elements = map)
                } else {
                    position
                }
            }
        )
}

fun <InteractionTarget : Any> Spotlight<InteractionTarget>.deselect(
    animationSpec: AnimationSpec<Float> = defaultAnimationSpec,
    mode: Operation.Mode = Operation.Mode.IMPOSED
) {
    operation(Deselect(mode), animationSpec)
}
