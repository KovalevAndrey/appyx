package com.bumble.appyx.navmodel.cards

import com.bumble.appyx.core.navigation.NavElement
import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.navmodel.cards.Cards.TransitionState

typealias CardsElement<T> = NavElement<T, TransitionState>

typealias CardsElements<T> = NavElements<T, TransitionState>
