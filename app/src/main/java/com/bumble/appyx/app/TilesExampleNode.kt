package com.bumble.appyx.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.app.TilesExampleNode.Routing.Child1
import com.bumble.appyx.app.node.child.GenericChildNode
import com.bumble.appyx.core.composable.Child
import com.bumble.appyx.core.composable.visibleChildrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.routingsource.tiles.Tiles
import com.bumble.appyx.routingsource.tiles.operation.deselect
import com.bumble.appyx.routingsource.tiles.operation.removeSelected
import com.bumble.appyx.routingsource.tiles.operation.select
import com.bumble.appyx.routingsource.tiles.operation.toggleSelection
import com.bumble.appyx.routingsource.tiles.transitionhandler.rememberTilesTransitionHandler
import kotlinx.coroutines.delay
import kotlin.random.Random

class TilesExampleNode(
    buildContext: BuildContext,
    private val tiles: Tiles<Routing> = Tiles(
        initialItems = listOf(
            Child1(),
            Child1(),
            Child1(),
            Child1(),
            Child1(),
            Child1()
        )
    ),
) : ParentNode<TilesExampleNode.Routing>(
    routingSource = tiles,
    buildContext = buildContext,
) {


    init {
        lifecycle.coroutineScope.launchWhenCreated {
            delay(2000)
            tiles.select(tiles.elements.value[4].key)
            delay(150)
            tiles.select(tiles.elements.value[3].key)
            delay(150)
            tiles.select(tiles.elements.value[2].key)
            delay(150)
            tiles.select(tiles.elements.value[5].key)
            delay(150)
            tiles.select(tiles.elements.value[1].key)
            delay(150)
            tiles.select(tiles.elements.value[0].key)
            delay(500)
            tiles.deselect(tiles.elements.value[5].key)
            delay(150)
            tiles.deselect(tiles.elements.value[1].key)
            delay(150)
            tiles.deselect(tiles.elements.value[0].key)
            delay(550)
            tiles.removeSelected()
        }
    }


    sealed class Routing {
        class Child1(val value: Int = Random.nextInt(0, 50)) : Routing()
    }


    override fun resolve(routing: Routing, buildContext: BuildContext): Node =
        when (routing) {
            is Child1 -> GenericChildNode(buildContext, counterStartValue = Random.nextInt(0, 50))
        }

    @Composable
    override fun View(modifier: Modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { tiles.removeSelected() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Remove selected", color = Color.Black)
            }

            val elements by tiles.visibleChildrenAsState()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {

                items(elements) { element ->
                    Child(
                        routingElement = element,
                        transitionHandler = rememberTilesTransitionHandler()
                    ) { child, _ ->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    tiles.toggleSelection(element.key)
                                }
                            )
                        ) {
                            child()
                        }
                    }
                }
            }

        }
    }
}
