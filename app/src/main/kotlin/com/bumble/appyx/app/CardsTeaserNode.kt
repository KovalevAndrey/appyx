package com.bumble.appyx.app

import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.R
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.navmodel.cards.operation.indicateLike
import com.bumble.appyx.navmodel.cards.operation.indicatePass
import com.bumble.appyx.navmodel.cards.operation.voteLike
import com.bumble.appyx.navmodel.cards.operation.votePass
import com.bumble.appyx.navmodel.cards.transitionhandler.rememberCardsTransitionHandler
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

class CardsTeaserNode(
    buildContext: BuildContext,
    private val cards: Cards<NavTarget> = Cards(
        initialItems = listOf(
            NavTarget.Michaelle,
            NavTarget.Lara,
            NavTarget.Victoria,
            NavTarget.Jack,
            NavTarget.Kane,
            NavTarget.Jessica,
            NavTarget.Michaelle,
            NavTarget.Lara,
            NavTarget.Victoria,
            NavTarget.Jack,
            NavTarget.Kane,
            NavTarget.Jessica,
        )
    ),
) : ParentNode<CardsTeaserNode.NavTarget>(
    buildContext = buildContext,
    navModel = cards
) {

    init {
        lifecycle.coroutineScope.launchWhenCreated {
            delay(1000)
            repeat(3) {
                delay(2000)
                cards.indicateLike()
                delay(1000)
                cards.indicatePass()
                delay(1000)
                cards.votePass()
                delay(1000)
                cards.voteLike()
                delay(500)
                cards.voteLike()
                delay(500)
                cards.voteLike()
            }
        }
    }

    @Parcelize
    sealed class NavTarget(val url: Int, val name: String, val age: Int) : Parcelable {

        @Parcelize
        object Michaelle : NavTarget(
            url = R.drawable.persona11,
            name = "Michaelle",
            age = 21
        )

        @Parcelize
        object Lara : NavTarget(
            url = R.drawable.persona2,
            name = "Lara",
            age = 19
        )

        @Parcelize
        object Victoria : NavTarget(
            url = R.drawable.persona7,
            name = "Victoria",
            age = 25
        )

        @Parcelize
        object Jack : NavTarget(
            url = R.drawable.persona16,
            name = "Jessica",
            age = 24
        )

        @Parcelize
        object Kane : NavTarget(
            url = R.drawable.persona13,
            name = "Kane",
            age = 27
        )

        @Parcelize
        object Jessica : NavTarget(
            url = R.drawable.persona29,
            name = "Jessica",
            age = 27
        )
    }


    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        ProfileCardNode(
            imageUrl = navTarget.url,
            name = navTarget.name,
            age = navTarget.age,
            buildContext = buildContext
        )

    @Composable
    override fun View(modifier: Modifier) {
        val padding = remember { 20.dp }
        Children(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(padding),
            navModel = cards,
            transitionHandler = rememberCardsTransitionHandler(padding) { spring(stiffness = Spring.StiffnessVeryLow) }
        )
    }
}

