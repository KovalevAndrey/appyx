package com.bumble.appyx.routingsource.modal

import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver
import com.bumble.appyx.routingsource.modal.Modal.TransitionState

object ModalOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            TransitionState.MODAL,
            TransitionState.FULL_SCREEN -> true
            TransitionState.CREATED,
            TransitionState.DESTROYED -> false
        }
}
