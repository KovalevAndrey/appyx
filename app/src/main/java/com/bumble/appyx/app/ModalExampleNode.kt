package com.bumble.appyx.app

import android.os.Parcelable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.node.child.GenericChildNode
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.composable.visibleChildrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.routingsource.modal.Modal
import com.bumble.appyx.routingsource.modal.ModalTransitionHandler
import com.bumble.appyx.routingsource.modal.operation.dismiss
import com.bumble.appyx.routingsource.modal.operation.fullScreen
import com.bumble.appyx.routingsource.modal.operation.show
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

class ModalExampleNode(
    buildContext: BuildContext,
    private val modal: Modal<Routing> = Modal(
        savedStateMap = buildContext.savedStateMap,
        initialElement = Routing.Child("first")
    )
) : ParentNode<ModalExampleNode.Routing>(
    routingSource = modal,
    buildContext = buildContext,
) {

    sealed class Routing(val name: String) : Parcelable {

        abstract val value: String

        @Parcelize
        data class Child(override val value: String) : Routing(value)
    }

    override fun resolve(routing: Routing, buildContext: BuildContext): Node =
        when (routing) {
            is Routing.Child -> GenericChildNode(buildContext, Random.nextInt(0, 50))
        }

    sealed class State {
        object Show : State()
        object Hide : State()
    }

    @Composable
    override fun View(modifier: Modifier) {
        Box(Modifier.fillMaxSize()) {

            val elements by modal.elements.collectAsState()
//            var state  remember {
//            mutableStateOf(State.Hide)
//        }

            val children by modal.visibleChildrenAsState()

//            val hasChildren = children[0].targetState != Modal.TransitionState.CREATED

                Children(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = animateColorAsState(
                                animationSpec = spring(stiffness = 200f),
                                targetValue = if (children.isNotEmpty()) Color(0xFF454545) else Color.White
                            ).value
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { modal.dismiss(elements.last().key) }
                        ),
                    routingSource = modal,
                    transitionHandler = remember { ModalTransitionHandler() }
                ) {
                    children<Routing> { child ->
                        child(
                            modifier = Modifier.clickable(enabled = false,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { }
                            )
                        )
                    }
                }


            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
            ) {
//                var fullScreenEnabled by remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        modal.show(elements.last().key)
                    }

                ) {
                    Text("Show modal", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        modal.fullScreen(elements.last().key)
                    }
                ) {
                    Text("Full screen", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        modal.dismiss(elements.last().key)
                    }
                ) {
                    Text("Dismiss", color = Color.Black)
                }
            }
        }
    }

}