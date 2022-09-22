package com.bumble.appyx.navmodel.cards.operation

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.navmodel.cards.Cards.Companion.FINAL_STATES
import com.bumble.appyx.navmodel.cards.CardsElements
import kotlinx.parcelize.Parcelize

@Parcelize
class VoteLike<T : Any> : CardsOperation<T> {

    override fun isApplicable(elements: CardsElements<T>): Boolean =
        true

    override fun invoke(
        elements: CardsElements<T>
    ): NavElements<T, Cards.TransitionState> {
        var found = false

        return elements.mapIndexed { index, element ->
            // Finds first element in the card stack which is not being removed
            if (!found && element.targetState !in FINAL_STATES) {
                found = true
                element.transitionTo(
                    newTargetState = Cards.TransitionState.VoteLike,
                    operation = this
                )
            } else {
                element
            }
        }
    }
}

fun <T : Any> Cards<T>.voteLike() {
    accept(VoteLike())
    promoteAll()
}
