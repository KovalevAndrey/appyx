package com.bumble.appyx.navmodel.cards

import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver
import com.bumble.appyx.navmodel.cards.Cards.TransitionState

internal object CardsOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            is TransitionState.Queued -> false
            is TransitionState.Bottom -> true
            is TransitionState.Top -> true
            is TransitionState.IndicateLike -> true
            is TransitionState.IndicatePass -> true
            is TransitionState.VoteLike -> false
            is TransitionState.VotePass -> false
        }
}
