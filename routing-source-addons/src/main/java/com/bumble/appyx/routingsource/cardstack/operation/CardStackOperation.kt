package com.bumble.appyx.routingsource.cardstack.operation

import com.bumble.appyx.core.routing.Operation
import com.bumble.appyx.routingsource.cardstack.CardStack

sealed interface CardStackOperation<Routing> : Operation<Routing, CardStack.TransitionState>