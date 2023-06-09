package com.bumble.appyx.components.spotlight.operation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.interactions.Parcelize
import com.bumble.appyx.interactions.core.Element
import com.bumble.appyx.interactions.core.model.transition.BaseOperation
import com.bumble.appyx.interactions.core.model.transition.Operation

@Parcelize
class Select<InteractionTarget>(
    override var mode: Operation.Mode = Operation.Mode.KEYFRAME
) : BaseOperation<SpotlightModel.State<InteractionTarget>>() {

    override fun isApplicable(state: SpotlightModel.State<InteractionTarget>): Boolean =
        state.positions[state.activeIndex.toInt()].elements.any { it.value == SpotlightModel.State.ElementState.STANDARD }

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
                        map.put(it, SpotlightModel.State.ElementState.SELECTED)
                    }
                    position.copy(elements = map)
                } else {
                    position
                }
            }
        )
}

fun <InteractionTarget : Any> Spotlight<InteractionTarget>.select(
    animationSpec: AnimationSpec<Float> = spring(stiffness = 10f),
    mode: Operation.Mode = Operation.Mode.KEYFRAME
) {
    operation(Select(mode), animationSpec)
}
