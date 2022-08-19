package com.bumble.appyx.routingsource.cardstack.operation

import com.bumble.appyx.routingsource.cardstack.CardStack
import com.bumble.appyx.routingsource.cardstack.CardStack.TransitionState
import com.bumble.appyx.routingsource.cardstack.CardStackElements
import kotlinx.parcelize.Parcelize

@Parcelize
class Discard<Routing> : CardStackOperation<Routing> {

    override fun isApplicable(elements: CardStackElements<Routing>): Boolean =
        elements.any { it.targetState is TransitionState.Active } &&
                elements.any { it.targetState is TransitionState.Stashed }

    override fun invoke(elements: CardStackElements<Routing>): CardStackElements<Routing> {

        val destroyIndex = elements.currentIndex
        val unStashIndex =
            elements.indexOfLast { it.targetState is TransitionState.Stashed }
        require(destroyIndex != -1) { "Nothing to destroy, state=$elements" }
        require(unStashIndex != -1) { "Nothing to remove from stash, state=$elements" }
        return elements.mapIndexed { index, element ->
            when (index) {
                destroyIndex -> element.transitionTo(
                    newTargetState = TransitionState.Destroyed,
                    operation = this
                )
                unStashIndex -> element.transitionTo(
                    newTargetState = TransitionState.Active,
                    operation = this
                )
                else -> {
                    val targetState = element.targetState
                    if (targetState is TransitionState.Stashed) {
                        element.transitionTo(
                            newTargetState = TransitionState.Stashed(targetState.stashDepth - 1),
                            operation = this@Discard
                        )
                    } else {
                        element
                    }
                }
            }
        }
    }

    private val <T> CardStackElements<T>.currentIndex: Int
        get() = this.indexOfLast { it.targetState is TransitionState.Active }
}

fun <T : Any> CardStack<T>.discard() {
    accept(Discard())
}