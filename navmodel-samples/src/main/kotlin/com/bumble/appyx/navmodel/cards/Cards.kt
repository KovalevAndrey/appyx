package com.bumble.appyx.navmodel.cards

import com.bumble.appyx.core.navigation.BaseNavModel
import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.core.navigation.Operation.Noop
import com.bumble.appyx.navmodel.cards.Cards.TransitionState
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Bottom
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Queued
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.Top
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.VoteLike
import com.bumble.appyx.navmodel.cards.Cards.TransitionState.VotePass

class Cards<T : Any>(
    initialItems: List<T> = listOf(),
) : BaseNavModel<T, TransitionState>(
    screenResolver = CardsOnScreenResolver,
    finalStates = FINAL_STATES,
    savedStateMap = null
) {
    companion object {
        internal val FINAL_STATES = setOf(VoteLike, VotePass)
    }

    sealed class TransitionState {
        data class Queued(val queueNumber: Int) : TransitionState()
        object Bottom : TransitionState()
        object Top : TransitionState()
        object IndicateLike : TransitionState()
        object IndicatePass : TransitionState()
        object VoteLike : TransitionState()
        object VotePass : TransitionState()

        fun next(): TransitionState =
            when (this) {
                is Queued -> if (queueNumber == 0) Bottom else Queued(queueNumber - 1)
                is Bottom -> Top
                else -> this
            }
    }

    override val initialElements = initialItems.mapIndexed { index, element ->
        val state = when (index) {
            0 -> Top
            1 -> Bottom
            else -> Queued(index - 2)
        }
        CardsElement(
            key = NavKey(element),
            fromState = if (state == Top) Bottom else state,
            targetState = state,
            operation = Noop()
        )
    }
}
