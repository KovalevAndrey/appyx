package com.bumble.appyx.routingsource.cardstack.backpresshandler

import com.bumble.appyx.core.routing.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.routingsource.cardstack.CardStack
import com.bumble.appyx.routingsource.cardstack.CardStackElements
import com.bumble.appyx.routingsource.cardstack.operation.Discard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiscardBackPressHandler<Routing : Any> :
    BaseBackPressHandlerStrategy<Routing, CardStack.TransitionState>() {

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        routingSource.elements.map(::areThereStashedElements)
    }

    private fun areThereStashedElements(elements: CardStackElements<Routing>) =
        elements.any { it.targetState is CardStack.TransitionState.Stashed }

    override fun onBackPressed() {
        routingSource.accept(Discard())
    }
}