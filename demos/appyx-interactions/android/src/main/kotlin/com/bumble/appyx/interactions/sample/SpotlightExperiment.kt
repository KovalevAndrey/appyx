package com.bumble.appyx.interactions.sample

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.ui.sliderscale.SpotlightSliderScale
import com.bumble.appyx.interactions.AppyxLogger
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.gesture.GestureSettleConfig
import com.bumble.appyx.interactions.core.ui.helper.AppyxComponentSetup
import com.bumble.appyx.interactions.core.ui.output.ElementUiModel
import com.bumble.appyx.interactions.sample.android.Element
import com.bumble.appyx.interactions.sample.android.SampleChildren
import com.bumble.appyx.interactions.theme.appyx_dark
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.Profile.Companion.profile002
import com.bumble.appyx.transitionmodel.BaseMotionController
import com.bumble.appyx.interactions.sample.InteractionTarget as Target

@ExperimentalMaterialApi
@Composable
@Suppress("LongMethod", "MagicNumber")
fun SpotlightExperiment(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    reverseOrientation: Boolean = false,
    motionController: (UiContext) -> BaseMotionController<Target, SpotlightModel.State<Target>, *, *>
) {
    val items = listOf(
        Target.Child6,
        Target.Child1,
        Target.Child2,
        Target.Child5,
        Target.Child3,
        Target.Child4,
        Target.Child2
//        Target.Child7,
//        Target.Child1,
//        Target.Child2,
//        Target.Child3,
//        Target.Child4,
//        Target.Child5,
//        Target.Child6,
//        Target.Child7,
//        Target.Child1,
//        Target.Child2,
//        Target.Child3,
//        Target.Child4,
//        Target.Child5,
//        Target.Child6,
//        Target.Child7,
    )
    val spotlight = Spotlight(
        model = SpotlightModel(
            items = items,
            savedStateMap = null
        ),
        motionController = motionController,
        gestureFactory = { SpotlightSliderScale.Gestures(it, orientation, reverseOrientation) },
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow / 4),
        gestureSettleConfig = GestureSettleConfig(
            completionThreshold = 0.2f,
            completeGestureSpec = spring(),
            revertGestureSpec = spring(),
        ),
    )

    AppyxComponentSetup(spotlight)

    Column(
        modifier
            .fillMaxWidth()
            .background(appyx_dark)
    ) {
        SpotlightUi(
            spotlight = spotlight,
//            modifier = Modifier.weight(0.9f)
        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.1f)
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(onClick = {
//                spotlight.updateElements(
//                    items,
//                    animationSpec = spring(stiffness = Spring.StiffnessVeryLow / 20)
//                )
//            }) {
//                Text("New")
//            }
//            Button(onClick = { spotlight.selected() }) {
//                Text("Selected")
//            }
//            Button(onClick = { spotlight.deselected() }) {
//                Text("Deselected")
//            }
//            Button(onClick = { spotlight.next() }) {
//                Text("Next")
//            }
//            Button(onClick = { spotlight.last() }) {
//                Text("Last")
//            }
//        }
    }
}

val map: MutableMap<ElementUiModel<*>, Profile> = mutableMapOf()

var index = 0

@Composable
fun generateProfile(element: ElementUiModel<*>): Profile {
    val current = map[element]
    if (current != null) return current
    val profile = Profile.allProfiles[index++]
    map[element] = profile
    return profile
}

@Composable
fun <InteractionTarget : Any> SpotlightUi(
    spotlight: Spotlight<InteractionTarget>,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    SampleChildren(
        clipToBounds = false,
        appyxComponent = spotlight,
        modifier = modifier
            .padding(
//                horizontal = 64.dp,
//                vertical = 12.dp
            ),
        element = { index, elementUiModel ->
            Element(
                color = color,
                elementUiModel = elementUiModel,
                contentDescription =
                "${SPOTLIGHT_EXPERIMENT_TEST_HELPER}_${elementUiModel.element.id}",
                profile = when (elementUiModel.element.interactionTarget) {
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child1 -> Profile.profile2001
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child2 -> Profile.profile1001
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child3 -> Profile.profile1003
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child4 -> Profile.profile2001
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child5 -> Profile.profile2004
                    com.bumble.appyx.interactions.sample.InteractionTarget.Child6 -> Profile.profile2003
                    else -> profile002
                },
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(elementUiModel.element.id) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                spotlight.onDrag(dragAmount, this)
                            },
                            onDragEnd = {
                                AppyxLogger.d("drag", "end")
                                spotlight.onDragEnd()
                            }
                        )
                    }
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpotlightExperimentInVertical(
    motionController: (UiContext) -> BaseMotionController<Target, SpotlightModel.State<Target>, *, *>
) {
    SpotlightExperiment(
        orientation = Orientation.Vertical,
        reverseOrientation = true,
        motionController = motionController
    )
}

const val SPOTLIGHT_EXPERIMENT_TEST_HELPER = "TheChild"

