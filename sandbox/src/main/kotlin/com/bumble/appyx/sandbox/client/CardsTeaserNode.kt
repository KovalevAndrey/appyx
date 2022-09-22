package com.bumble.appyx.sandbox.client

import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
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
import com.bumble.appyx.sandbox.client.CardsTeaserNode.NavTarget
import com.bumble.appyx.sandbox.client.profilecard.ProfileCardNode
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

class CardsTeaserNode(
    buildContext: BuildContext,
    private val cards: Cards<NavTarget> = Cards(
        initialItems = listOf(
            NavTarget.Michaelle,
            NavTarget.Lara,
            NavTarget.Victoria,
            NavTarget.Jessica,
            NavTarget.Kane,
            NavTarget.Michaelle,
            NavTarget.Lara,
            NavTarget.Victoria,
            NavTarget.Jessica,
            NavTarget.Kane
        )
    ),
) : ParentNode<NavTarget>(
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
    sealed class NavTarget(val url: String, val name: String, val age: Int) : Parcelable {

        @Parcelize
        object Michaelle : NavTarget(
            url = "https://images.pexels.com/photos/1572878/pexels-photo-1572878.jpeg?auto=compress&cs=tinysrgb&w=1600",
            name = "Michaelle",
            age = 21
        )

        @Parcelize
        object Lara : NavTarget(
            url = "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
            name = "Lara",
            age = 19
        )

        @Parcelize
        object Victoria : NavTarget(
            url = "https://images.pexels.com/photos/2235071/pexels-photo-2235071.jpeg?auto=compress&cs=tinysrgb&w=1600",
            name = "Victoria",
            age = 25
        )

        @Parcelize
        object Jessica : NavTarget(
            url = "https://images.pexels.com/photos/2811089/pexels-photo-2811089.jpeg?auto=compress&cs=tinysrgb&w=1600",
            name = "Jessica",
            age = 24
        )

        @Parcelize
        object Kane : NavTarget(
            url = "https://images.pexels.com/photos/428340/pexels-photo-428340.jpeg?auto=compress&cs=tinysrgb&w=1600",
            name = "Kane",
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
                .padding(padding),
            navModel = cards,
            transitionHandler = rememberCardsTransitionHandler(padding) { spring(stiffness = Spring.StiffnessVeryLow) }
        )
    }
}

