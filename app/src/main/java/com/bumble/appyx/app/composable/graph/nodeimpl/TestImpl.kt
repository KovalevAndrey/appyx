package com.bumble.appyx.app.composable.graph.nodeimpl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.composable.graph.GraphNode
import com.bumble.appyx.app.ui.*
import kotlin.random.Random

class TestImpl(
    private val id: Int,
    private val childLevels: Int,
) : GraphNode {

    override val isActive: MutableState<Boolean> =
        mutableStateOf(false)

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

    private val color = colors[Random.nextInt(colors.size)]

    override fun children(): List<GraphNode> =
        if (childLevels == 0) emptyList() else listOf(
            TestImpl(1, childLevels - 1),
            TestImpl(2, childLevels - 1),
        )

    @Composable
    override fun View(modifier: Modifier) {
        Box(
            modifier = modifier
                .size(30.dp)
                .padding(top = 2.dp, start = 4.dp, end = 4.dp, bottom = 2.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(4.dp)
        ) {
            androidx.compose.material.Text(
                text = "$id",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview
@Composable
@ExperimentalComposeUiApi
fun TestImplPreview() {
//    Tree(TestImpl(1, 2))
}
