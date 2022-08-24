package com.bumble.appyx.app.node.onboarding.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.composable.graph.GraphNode
import com.bumble.appyx.app.composable.graph.Tree
import com.bumble.appyx.app.composable.graph.nodeimpl.SimpleGraphNode
import com.bumble.appyx.app.composable.graph.nodeimpl.colors
import com.bumble.appyx.app.ui.AppyxSampleAppTheme
import com.bumble.appyx.app.ui.md_blue_300
import com.bumble.appyx.app.ui.md_cyan_300
import com.bumble.appyx.app.ui.md_pink_300
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.modality.BuildContext.Companion.root
import com.bumble.appyx.core.node.Node
import kotlinx.coroutines.delay

@ExperimentalUnitApi
@ExperimentalComposeUiApi
class ApplicationTree(
    buildContext: BuildContext
) : Node(
    buildContext = buildContext,
) {
    private val login = SimpleGraphNode("Login")
    private val r1 = SimpleGraphNode("R1")
    private val r2 = SimpleGraphNode("R2")
    private val r3 = SimpleGraphNode("R3")
    private val register = SimpleGraphNode(
        label = "Register",
        children = listOf(
            r1,
            r2,
            r3,
        )
    )
    private val loggedOut = SimpleGraphNode(
        color = md_pink_300,
        label = "LoggedOut",
        children = listOf(
            register,
            login
        )
    )
    private val o1 = SimpleGraphNode("O1")
    private val o2 = SimpleGraphNode("O2")
    private val o3 = SimpleGraphNode("O3")
    private val onboarding = SimpleGraphNode(
        label = "Onboarding",
        children = listOf(
            o1,
            o2,
            o3,
        )
    )
    private val c1 = SimpleGraphNode(label = "C1", color = colors[7])
    private val c2 = SimpleGraphNode(label = "C2", color = colors[6])
    private val c3 = SimpleGraphNode(color = colors[5], label = "C3")
    private val dating = SimpleGraphNode(
        color = colors[4],
        label = "Dating",
        children = listOf(
            c1,
            c2,
            c3,
        )
    )
    private val messages = SimpleGraphNode(color = colors[3], label = "Messages")
    private val main = SimpleGraphNode(
        color = colors[8],
        label = "Main",
        children = listOf(
            dating,
            messages,
        )
    )
    private val loggedIn = SimpleGraphNode(
        color = md_cyan_300,
        label = "LoggedIn",
        children = listOf(
            main,
            onboarding,
        )
    )
    private val root: GraphNode = SimpleGraphNode(
        color = md_blue_300,
        label = "Root",
        children = listOf(
            loggedOut,
            loggedIn,
        )
    )

    @Composable
    override fun View(modifier: Modifier) {
        Box(modifier = Modifier.fillMaxSize()) {

            Tree(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                graphNode = root
            )
        }

        fun deactivateSubtree(subtreeRoot: GraphNode) {
            subtreeRoot.isActive.value = false
            val children = subtreeRoot.children()
            if (children.isNotEmpty()) {
                children.forEach {
                    deactivateSubtree(it)
                }
            }
        }

        fun activateLeafNode(
            root: GraphNode,
            graphNodeToActivate: GraphNode,
            path: List<GraphNode> = listOf()
        ): List<GraphNode>? {
            val children = root.children()
            val newPath = path.toMutableList().apply { add(root) }
            if (graphNodeToActivate == root) {
                return newPath
            } else if (children.isEmpty()) {
                return null
            } else {
                children.forEach {
                    val childResult = activateLeafNode(it, graphNodeToActivate, newPath)
                    if (childResult != null) {
                        return childResult
                    }
                }
            }
            return null
        }

        fun activateNode(node: GraphNode) {
            deactivateSubtree(root)
            activateLeafNode(root, node)?.forEach { it.isActive.value = true }
        }

        LaunchedEffect(Unit) {
            val startDelay: Long = 500
            val intervalDelay: Long = 1200

            while (true) {

                deactivateSubtree(root)

                delay(startDelay)
                activateNode(r1)
                delay(intervalDelay)
                activateNode(r2)
                delay(intervalDelay)
                activateNode(r3)

                delay(intervalDelay)
                activateNode(login)

                delay(intervalDelay)
                activateNode(c1)

                delay(intervalDelay)
                activateNode(c2)

                delay(intervalDelay)
                activateNode(c3)

                delay(intervalDelay)
                activateNode(messages)

                delay(intervalDelay)
                activateNode(o1)
                delay(intervalDelay)
                activateNode(o2)
                delay(intervalDelay)
                activateNode(o3)

                delay(intervalDelay * 2)
            }
        }
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
fun ApplicationTreePreview() {
    AppyxSampleAppTheme(darkTheme = false) {
        PreviewContent()
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
fun ApplicationTreePreviewDark() {
    AppyxSampleAppTheme(darkTheme = true) {
        PreviewContent()
    }
}

@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
private fun PreviewContent() {
    Surface(color = MaterialTheme.colors.background) {
        Box(Modifier.fillMaxSize()) {
            NodeHost(integrationPoint = IntegrationPointStub()) {
                ApplicationTree(
                    root(null),
                )
            }
        }
    }
}
