package com.bumble.appyx.routingsource.cardstack.operation

import com.bumble.appyx.core.routing.RoutingKey
import com.bumble.appyx.routingsource.cardstack.CardStack
import com.bumble.appyx.routingsource.cardstack.CardStack.TransitionState
import com.bumble.appyx.routingsource.cardstack.CardStackElement
import com.bumble.appyx.routingsource.cardstack.CardStackElements
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class Draw<Routing>(
    private val element: @RawValue Routing
) : CardStackOperation<Routing> {

    override fun isApplicable(elements: CardStackElements<Routing>): Boolean =
        element != elements.current?.key?.routing

    override fun invoke(elements: CardStackElements<Routing>): CardStackElements<Routing> {

        var stashCount = elements.count { it.targetState is TransitionState.Stashed }

        return elements.map {
            when {
                it.targetState is TransitionState.Active -> {
                    it.transitionTo(
                        newTargetState = TransitionState.Stashed(0),
                        operation = this
                    )
                }
                it.targetState is TransitionState.Stashed && it.fromState is TransitionState.Stashed -> {
                    it.transitionTo(
                        newTargetState = TransitionState.Stashed((it.fromState as TransitionState.Stashed).stashDepth + 1),
                        operation = this
                    )
                }
                else -> it
            }
        } + CardStackElement(
            key = RoutingKey(element),
            fromState = TransitionState.Created,
            targetState = TransitionState.Active,
            operation = this
        )
    }

    private val <T> CardStackElements<T>.current: CardStackElement<T>?
        get() = this.lastOrNull { it.targetState is TransitionState.Active }
}

fun <T : Any> CardStack<T>.draw(element: T) {
    accept(Draw(element))
}