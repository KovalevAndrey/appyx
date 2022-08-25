package com.bumble.appyx.app.node.onboarding

import android.os.Parcelable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.composable.SpotlightDotsIndicator
import com.bumble.appyx.app.node.onboarding.OnboardingContainerNode.Routing
import com.bumble.appyx.app.node.onboarding.screen.*
import com.bumble.appyx.app.ui.AppyxSampleAppTheme
import com.bumble.appyx.app.ui.appyx_dark
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.modality.BuildContext.Companion.root
import com.bumble.appyx.core.navigation.gesture.swipe.swipeNavigationModifier
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.spotlight.Spotlight
import com.bumble.appyx.navmodel.spotlight.backpresshandler.GoToPrevious
import com.bumble.appyx.navmodel.spotlight.gesturehandler.rememberSpotlightSwipeGestureHandler
import com.bumble.appyx.navmodel.spotlight.hasNext
import com.bumble.appyx.navmodel.spotlight.hasPrevious
import com.bumble.appyx.navmodel.spotlight.operation.next
import com.bumble.appyx.navmodel.spotlight.operation.previous
import com.bumble.appyx.navmodel.spotlight.transitionhandler.rememberSpotlightSlider
import kotlinx.parcelize.Parcelize

@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class OnboardingContainerNode(
    buildContext: BuildContext,
    private val spotlight: Spotlight<Routing> = Spotlight(
        items = listOf(
            Routing.IntroScreen,
            Routing.ApplicationTree,
            Routing.StatefulNode1,
            Routing.StatefulNode2,
            Routing.NavModelTeaser,
        ),
        backPressHandler = GoToPrevious(),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(
    navModel = spotlight,
    buildContext = buildContext
) {

    sealed class Routing : Parcelable {
        @Parcelize
        object IntroScreen : Routing()

        @Parcelize
        object ApplicationTree : Routing()

        @Parcelize
        object StatefulNode1 : Routing()

        @Parcelize
        object StatefulNode2 : Routing()

        @Parcelize
        object NavModelTeaser : Routing()
    }

    override fun resolve(routing: Routing, buildContext: BuildContext): Node =
        when (routing) {
            Routing.IntroScreen -> IntroScreen(buildContext)
            Routing.ApplicationTree -> ApplicationTree(buildContext)
            Routing.StatefulNode1 -> StatefulNode1(buildContext)
            Routing.StatefulNode2 -> StatefulNode2(buildContext)
            Routing.NavModelTeaser -> NavModelTeaserNode(buildContext)
        }

    @Composable
    override fun View(modifier: Modifier) {
        val hasPrevious = spotlight.hasPrevious().collectAsState(initial = false)
        val hasNext = spotlight.hasNext().collectAsState(initial = false)
        val previousVisibility = animateFloatAsState(
            targetValue = if (hasPrevious.value) 1f else 0f
        )

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Children(
                modifier = Modifier
                    .weight(1f)
                    .swipeNavigationModifier(rememberSpotlightSwipeGestureHandler(spotlight))
                    .fillMaxWidth(),
                transitionHandler = rememberSpotlightSlider(clipToBounds = true),
                navModel = spotlight
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                SpotlightDotsIndicator(
                    spotlight = spotlight
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (hasNext.value) {
                        TextButton(
                            modifier = Modifier.alpha(previousVisibility.value),
                            enabled = hasPrevious.value,
                            onClick = { spotlight.previous() }
                        ) {
                            Text(
                                text = "Previous".toUpperCase(Locale.current),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        TextButton(
                            onClick = { spotlight.next() }
                        ) {
                            Text(
                                text = "Next".toUpperCase(Locale.current),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Spacer(Modifier)
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { finish() }
                        ) {
                            Text(
                                text = "Check it out!",
                                color = appyx_dark,
                            )
                        }
                        Spacer(Modifier)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun OnboardingContainerNodePreview() {
    AppyxSampleAppTheme(darkTheme = false) {
        PreviewContent()
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun OnboardingContainerNodePreviewDark() {
    AppyxSampleAppTheme(darkTheme = true) {
        PreviewContent()
    }
}

@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun PreviewContent() {
    Surface(color = MaterialTheme.colors.background) {
        Box(Modifier.fillMaxSize()) {
            NodeHost(integrationPoint = IntegrationPointStub()) {
                OnboardingContainerNode(root(null))
            }
        }
    }
}

