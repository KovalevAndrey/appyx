package com.bumble.appyx.demos.sandbox.navigation.node.spotlight.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.operation.first
import com.bumble.appyx.components.spotlight.operation.last
import com.bumble.appyx.components.spotlight.operation.next
import com.bumble.appyx.components.spotlight.operation.previous
import com.bumble.appyx.components.spotlight.ui.slider.SpotlightSlider
import com.bumble.appyx.demos.sandbox.navigation.colors
import com.bumble.appyx.demos.sandbox.navigation.composable.KnobControl
import com.bumble.appyx.demos.sandbox.navigation.node.spotlight.debug.SpotlightDebugNode.NavTarget
import com.bumble.appyx.demos.sandbox.navigation.ui.appyx_dark
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.navigation.node.node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize

class SpotlightDebugNode(
    nodeContext: NodeContext,
    private val model: SpotlightModel<NavTarget> = SpotlightModel(
        items = List(7) { NavTarget.Child(it + 1) },
        initialActiveIndex = 0f,
        savedStateMap = nodeContext.savedStateMap
    ),
    private val spotlight: Spotlight<NavTarget> = Spotlight(
        model = model,
        visualisation = { SpotlightSlider(it, model.currentState) },
        isDebug = true
    )
) : ParentNode<NavTarget>(
    nodeContext = nodeContext,
    appyxComponent = spotlight
) {

    init {
        spotlight.next()
        spotlight.next()
        spotlight.next()
        spotlight.previous()
        spotlight.last()
        spotlight.first()
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        class Child(val index: Int) : NavTarget()
    }

    override fun buildChildNode(navTarget: NavTarget, nodeContext: NodeContext): Node =
        when (navTarget) {
            is NavTarget.Child -> node(nodeContext) {
                val backgroundColor = remember { colors.shuffled().random() }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(5))
                        .background(backgroundColor)
                        .padding(24.dp)
                ) {
                    Text(
                        text = navTarget.index.toString(),
                        fontSize = 21.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    @ExperimentalMaterialApi
    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            modifier
                .fillMaxWidth()
                .background(appyx_dark)
        ) {
            KnobControl(onValueChange = {
                spotlight.setNormalisedProgress(it)
            })
            AppyxNavigationContainer(
                appyxComponent = spotlight,
                modifier = Modifier.padding(
                    horizontal = 64.dp,
                    vertical = 12.dp
                )
            )
        }
    }
}
