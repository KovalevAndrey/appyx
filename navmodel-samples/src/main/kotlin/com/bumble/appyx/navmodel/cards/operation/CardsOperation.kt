package com.bumble.appyx.navmodel.cards.operation

import com.bumble.appyx.core.navigation.Operation
import com.bumble.appyx.navmodel.cards.Cards

sealed interface CardsOperation<T> : Operation<T, Cards.TransitionState>
