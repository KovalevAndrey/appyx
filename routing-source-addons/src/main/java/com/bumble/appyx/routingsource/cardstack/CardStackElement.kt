package com.bumble.appyx.routingsource.cardstack

import com.bumble.appyx.core.routing.RoutingElement
import com.bumble.appyx.core.routing.RoutingElements

typealias CardStackElement<T> = RoutingElement<T, CardStack.TransitionState>

typealias CardStackElements<T> = RoutingElements<T, CardStack.TransitionState>