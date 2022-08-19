package com.bumble.appyx.app.node

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.node.BackStackNode.Routing
import com.bumble.appyx.app.node.child.GenericChildNode
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.routingsource.cardstack.CardStack
import com.bumble.appyx.routingsource.cardstack.operation.discard
import com.bumble.appyx.routingsource.cardstack.operation.draw
import com.bumble.appyx.routingsource.cardstack.transitionhandler.rememberCardStacker
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

class BackStackNode(
    buildContext: BuildContext,
    private val backStack: CardStack<Routing> = CardStack(
        initialElement = Routing.Child(),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(
    routingSource = backStack,
    buildContext = buildContext
) {

    override fun resolve(routing: Routing, buildContext: BuildContext): Node {
        return when (routing) {
            is Routing.Child -> GenericChildNode(buildContext, Random.nextInt(0, 100))
        }
    }

    sealed class Routing : Parcelable {

        @Parcelize
        data class Child(val value: Int = Random.nextInt(0, 100)) : Routing()
    }

    @Composable
    override fun View(modifier: Modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Children(
                modifier = Modifier
                    .fillMaxSize(),
//                    .padding(start = 30.dp),
                routingSource = backStack,
                transitionHandler = rememberCardStacker()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Button(onClick = { backStack.draw(Routing.Child()) }) {
                    Text(text = "Draw", color = Color.Black)
                }
                Spacer(modifier = Modifier.requiredWidth(16.dp))
                Button(onClick = { backStack.discard() }) {
                    Text(text = "Discard", color = Color.Black)
                }
            }
        }
    }


}