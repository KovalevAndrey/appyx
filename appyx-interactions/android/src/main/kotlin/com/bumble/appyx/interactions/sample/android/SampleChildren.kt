package com.bumble.appyx.interactions.sample.android

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumble.appyx.interactions.core.model.BaseAppyxComponent
import com.bumble.appyx.interactions.core.ui.output.ElementUiModel
import com.bumble.appyx.interactions.sample.Children
import com.bumble.appyx.interactions.sample.SampleElement
import com.bumble.appyx.interactions.sample.colors
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.Profile.Companion.allProfiles
import kotlin.math.roundToInt

/**
 * Renders children with the [SampleElement] composable.
 *
 * For real-life use-cases use the [Children] wrapper directly.
 */
@Composable
fun <InteractionTarget : Any, ModelState : Any> SampleChildren(
    appyxComponent: BaseAppyxComponent<InteractionTarget, ModelState>,
    modifier: Modifier = Modifier,
    clipToBounds: Boolean = false,
    element: @Composable (Int, ElementUiModel<InteractionTarget>) -> Unit = { index, elementUiModel ->
        SampleElement(index = index, colors = colors, elementUiModel = elementUiModel)
    },
) {
    Children(
        appyxComponent = appyxComponent,
        screenWidthPx = (LocalConfiguration.current.screenWidthDp * LocalDensity.current.density).roundToInt(),
        screenHeightPx = (LocalConfiguration.current.screenHeightDp * LocalDensity.current.density).roundToInt(),
        modifier = modifier,
        clipToBounds = clipToBounds,
        childWrapper = { index, elementUiModel ->
            element(index, elementUiModel)
        },
    )
}


@Composable
fun Element(
    elementUiModel: ElementUiModel<*>,
    modifier: Modifier,
    profile: Profile = remember { allProfiles.random() },
    color: Color? = Color.Unspecified,
    contentDescription: String? = null
) {
    Card(
        profile = profile,
        elementUiModel = elementUiModel,
        modifier = modifier,
        colors = colors,
        color = color,
        contentDescription = contentDescription,
    )
}


@Composable
fun Card(
    elementUiModel: ElementUiModel<*>,
    modifier: Modifier = Modifier.fillMaxSize(),
    profile: Profile = remember { allProfiles.random() },
    colors: List<Color>,
    color: Color? = Color.Unspecified,
    contentDescription: String? = null
) {
    val backgroundColor = remember {
        if (color == Color.Unspecified) colors.shuffled().random() else color ?: Color.Unspecified
    }

    BoxWithConstraints(
        modifier = Modifier
            .then(elementUiModel.modifier)
//            .clip(RoundedCornerShape(5))
//            .then(if (color == null) Modifier else Modifier.background(backgroundColor))
            .then(modifier)
            .semantics {
                contentDescription?.let { this.contentDescription = it }
            }
    ) {
        val widthDp = LocalConfiguration.current.screenWidthDp
        val isFullWidth = this.maxWidth.value.toInt() == LocalConfiguration.current.screenWidthDp
        val percetage by remember {
            derivedStateOf {
                this.maxWidth.value / widthDp
            }
        }
        Column {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profile.drawableRes)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }


        val percent =
            (this.maxWidth.value / widthDp.toFloat() - 0.65f) / (1.0f - 0.65f).coerceIn(
                0f..1f
            )

        val percentCoerced = percent.coerceIn(1f..1f)

//        if (isFullWidth) {
        Column(
            modifier = Modifier
                .alpha(percent)
                .align(Alignment.BottomStart)
                .padding(start = (24.dp * percentCoerced), bottom = (36.dp * percentCoerced))
        ) {
            Text(
                text = "${profile.name}, ${profile.age}",
                color = Color.White,
                fontSize = 30.sp * percentCoerced
            )
            Spacer(modifier = Modifier.requiredHeight(4.dp))
            Text(text = profile.city, color = Color.White, fontSize = 20.sp * percentCoerced)
        }
//        }
    }
}

