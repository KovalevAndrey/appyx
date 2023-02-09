package com.bumble.appyx.transitionmodel

//import com.bumble.appyx.interactions.Logger
import DefaultAnimationSpec
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import com.bumble.appyx.interactions.Logger
import com.bumble.appyx.interactions.core.Segment
import com.bumble.appyx.interactions.core.TransitionModel
import com.bumble.appyx.interactions.core.Update
import com.bumble.appyx.interactions.core.ui.BaseProps
import com.bumble.appyx.interactions.core.ui.FrameModel
import com.bumble.appyx.interactions.core.ui.Interpolator
import com.bumble.appyx.interactions.core.ui.MatchedProps
import com.bumble.appyx.interactions.core.ui.property.Animatable
import com.bumble.appyx.interactions.core.ui.property.HasModifier
import com.bumble.appyx.interactions.core.ui.property.Interpolatable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseInterpolator<NavTarget : Any, ModelState, Props>(
    private val defaultAnimationSpec: SpringSpec<Float> = DefaultAnimationSpec,
    private val coroutineScope: CoroutineScope
) : Interpolator<NavTarget, ModelState> where Props : BaseProps, Props : HasModifier, Props : Interpolatable<Props>, Props : Animatable<Props> {

    private val cache: MutableMap<String, Props> = mutableMapOf()
    private val animations: MutableMap<String, Boolean> = mutableMapOf()
    private val isAnimating: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var currentSpringSpec: SpringSpec<Float> = defaultAnimationSpec
    private var isDragging: Boolean = false

    abstract fun defaultProps(): Props

    override fun overrideAnimationSpec(springSpec: SpringSpec<Float>) {
        currentSpringSpec = springSpec
    }

    final override fun isAnimating(): StateFlow<Boolean> =
        isAnimating

    final override fun onStartDrag(position: Offset) {
        isDragging = true
    }

    final override fun onDrag(dragAmount: Offset, density: Density) {
        isDragging = true
    }

    final override fun onDragEnd(
        completionThreshold: Float,
        completeGestureSpec: AnimationSpec<Float>,
        revertGestureSpec: AnimationSpec<Float>
    ) {
        isDragging = false
    }

    final override fun applyGeometry(output: TransitionModel.Output<ModelState>) {
        if (isDragging) snapGeometry(output) else animateGeometry(output)
    }

    open fun snapGeometry(output: TransitionModel.Output<ModelState>) {}

    open fun animateGeometry(output: TransitionModel.Output<ModelState>) {}

    fun updateAnimationState(key: String, isAnimating: Boolean) {
        animations[key] = isAnimating
        this.isAnimating.update { isAnimating || animations.any { it.value } }
    }

    abstract fun ModelState.toProps(): List<MatchedProps<NavTarget, Props>>

    override fun mapUpdate(update: Update<ModelState>): List<FrameModel<NavTarget>> {
        val targetProps = update.currentTargetState.toProps()

        Logger.log("VIS", "--------------------------")

        // TODO: use a map instead of find
        return targetProps.map { t1 ->
            val elementProps = cache.getOrPut(t1.element.id) { defaultProps() }
            val visibleState = MutableStateFlow(t1.props.isVisible)
            Logger.log("VIS", "creating update FrameModel. isVisible: ${elementProps.isVisible}")
            FrameModel(
//                visibleState  = MutableStateFlow(true),
                visibleState = MutableStateFlow(t1.props.isVisible),
                navElement = t1.element,
                modifier = elementProps.modifier.composed {
                    LaunchedEffect(update) {
                        coroutineScope.launch {
                            if (update.animate) {
                                elementProps.animateTo(
                                    scope = this,
                                    props = t1.props,
                                    springSpec = currentSpringSpec,
                                    onStart = {
                                        Logger.log(
                                            "VIS",
                                            "starting update. isVisible: ${elementProps.isVisible || t1.props.isVisible}"
                                        )
                                        visibleState.update { elementProps.isVisible || t1.props.isVisible }
                                        updateAnimationState(t1.element.id, true)
                                    },
                                    onFinished = {
                                        visibleState.update { elementProps.isVisible }
                                        Logger.log(
                                            "VIS",
                                            "finishing update. isVisible: ${t1.props.isVisible}"
                                        )
                                        updateAnimationState(t1.element.id, false)
                                        currentSpringSpec = defaultAnimationSpec
                                    },
                                )
                            } else {
                                Logger.log(
                                    "VIS",
                                    "snapTo update. isVisible: ${elementProps.isVisible}"
                                )
                                elementProps.snapTo(this, t1.props)
                                visibleState.update { elementProps.isVisible }
                            }
                        }
                    }
                    this
                },
                progress = MutableStateFlow(1f),
            )
        }
    }

    override fun mapSegment(
        segment: Segment<ModelState>,
        segmentProgress: StateFlow<Float>
    ): List<FrameModel<NavTarget>> {
        val (fromState, targetState) = segment.navTransition
        val fromProps = fromState.toProps()
        val targetProps = targetState.toProps()

        // TODO: use a map instead of find
        return targetProps.map { t1 ->
            val t0 = fromProps.find { it.element.id == t1.element.id }!!
            val elementProps = cache.getOrPut(t1.element.id) { defaultProps() }
            //Synchronously apply current value to props before they reach composition to avoid jumping between default & current valu
            coroutineScope.launch {
                elementProps.lerpTo(t0.props, t1.props, segmentProgress.value)
            }

            val state = resolveNavElementVisibility(t0.props, t1.props, segmentProgress.value)
            Logger.log(
                "VIS",
                "map Segment. isVisible: $state, promProps: ${t0.props}, toProps: ${t1.props}, progress: ${segmentProgress.value}"
            )
            val visibleState = MutableStateFlow(
                state
            )

            FrameModel(
                visibleState = visibleState,
                navElement = t1.element,
                modifier = Modifier.interpolatedProps(
                    segmentProgress,
                    visibleState,
                    elementProps,
                    t0,
                    t1
                )
                    .then(elementProps.modifier),
                progress = segmentProgress,
            )
        }
    }

    private fun Modifier.interpolatedProps(
        segmentProgress: StateFlow<Float>,
        visibilityState: MutableStateFlow<Boolean>,
        elementProps: Props,
        from: MatchedProps<NavTarget, Props>,
        to: MatchedProps<NavTarget, Props>
    ): Modifier = composed {
        val progress by segmentProgress.collectAsState(segmentProgress.value)
        LaunchedEffect(progress) {
            elementProps.lerpTo(from.props, to.props, progress)
            visibilityState.update {
                from.props.isVisible || to.props.isVisible
            }
        }
        this
    }
}
