package com.bumble.appyx.app.node.child

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.app.ui.*
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.modality.BuildContext.Companion.root
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.state.MutableSavedStateMap
import kotlinx.coroutines.delay
import kotlin.random.Random

class GenericChildNode(
    buildContext: BuildContext,
    counterStartValue: Int
) : Node(
    buildContext = buildContext
) {

    companion object {
        private const val KEY_ID = "Id"
        private const val KEY_COUNTER = "Counter"
        private const val KEY_COLOR_INDEX = "ColorIndex"
    }

    private val colors = listOf(
        manatee,
        sizzling_red,
        atomic_tangerine,
        silver_sand,
        md_pink_500,
        md_indigo_500,
        md_blue_500,
        md_light_blue_500,
        md_cyan_500,
        md_teal_500,
        md_light_green_500,
        md_lime_500,
        md_amber_500,
        md_grey_500,
        md_blue_grey_500
    )

    private val id = Random.nextInt(10000)
    private var counter by mutableStateOf(
        buildContext.savedStateMap?.get(KEY_COUNTER) as? Int ?: counterStartValue
    )
    private var colorIndex by mutableStateOf(
        buildContext.savedStateMap?.get(KEY_COLOR_INDEX) as? Int ?: Random.nextInt(colors.size)
    )

    init {
        lifecycle.coroutineScope.launchWhenCreated {
            while (true) {
                counter++
                delay(1000)
            }
        }
    }

    override fun onSaveInstanceState(state: MutableSavedStateMap) {
        super.onSaveInstanceState(state)
        state[KEY_ID] = id
        state[KEY_COUNTER] = counter
        state[KEY_COLOR_INDEX] = colorIndex
    }

    @Composable
    override fun View(modifier: Modifier) {
        val color by derivedStateOf { colors[colorIndex] }
//        val text = remember { "Value: ${Random.nextInt(0, 100)}" }
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            Text(
                text = "$counter",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 50.sp
            )
        }
    }
}

@Preview
@Composable
fun GenericChildNodePreview() {
    Box(Modifier.size(200.dp)) {
        NodeHost(integrationPoint = IntegrationPointStub()) {
            GenericChildNode(root(null), 100)
        }
    }
}
