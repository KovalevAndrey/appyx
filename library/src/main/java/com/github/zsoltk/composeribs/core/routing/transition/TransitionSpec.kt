package com.github.zsoltk.composeribs.core.routing.transition

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable

typealias TransitionSpec<S, T> = @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<T>
