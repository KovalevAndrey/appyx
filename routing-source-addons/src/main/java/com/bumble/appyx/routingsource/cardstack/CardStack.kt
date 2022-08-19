package com.bumble.appyx.routingsource.cardstack

import com.bumble.appyx.core.routing.BaseRoutingSource
import com.bumble.appyx.core.routing.Operation
import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.core.routing.backpresshandlerstrategies.BackPressHandlerStrategy
import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.core.routing.operationstrategies.ExecuteImmediately
import com.bumble.appyx.core.routing.operationstrategies.OperationStrategy
import com.bumble.appyx.core.state.SavedStateMap
import com.bumble.appyx.routingsource.cardstack.backpresshandler.DiscardBackPressHandler

class CardStack<Routing : Any>(
    initialElement: Routing,
    savedStateMap: SavedStateMap?,
    key: String = "Key",
    backPressHandler: BackPressHandlerStrategy<Routing, TransitionState> = DiscardBackPressHandler(),
    operationStrategy: OperationStrategy<Routing, TransitionState> = ExecuteImmediately(),
    screenResolver: OnScreenStateResolver<TransitionState> = CardStackOnScreenResolver(),
) : BaseRoutingSource<Routing, CardStack.TransitionState>(
    backPressHandler = backPressHandler,
    screenResolver = screenResolver,
    operationStrategy = operationStrategy,
    finalState = TransitionState.Destroyed,
    savedStateMap = savedStateMap,
    key = key,
) {

    sealed class TransitionState {
        object Created : TransitionState()
        object Active : TransitionState()
        data class Stashed(val stashDepth: Int) : TransitionState()
        object Destroyed : TransitionState()
    }

    override val initialElements = listOf(
        CardStackElement(
            key = RoutingKey(initialElement),
            fromState = TransitionState.Created,
            targetState = TransitionState.Active,
            operation = Operation.Noop()
        )
    )
}