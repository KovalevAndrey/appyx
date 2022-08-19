package com.bumble.appyx.routingsource.cardstack

import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.routingsource.cardstack.CardStack.TransitionState

class CardStackOnScreenResolver(
    private val maxVisibleStashedItems: Int = 4
) : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            is TransitionState.Active -> true
            is TransitionState.Stashed -> isStashedVisible(state)
            is TransitionState.Created,
            is TransitionState.Destroyed -> false
        }

    private fun isStashedVisible(stashed: TransitionState.Stashed): Boolean =
        stashed.stashDepth < maxVisibleStashedItems
}