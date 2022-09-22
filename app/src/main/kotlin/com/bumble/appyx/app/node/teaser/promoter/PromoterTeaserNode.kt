package com.bumble.appyx.app.node.teaser.promoter

import android.os.Parcelable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.R
import com.bumble.appyx.app.ProfileCardNode
import com.bumble.appyx.app.node.teaser.promoter.PromoterTeaserNode.NavTarget
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.promoter.navmodel.Promoter
import com.bumble.appyx.navmodel.promoter.navmodel.operation.addFirst
import com.bumble.appyx.navmodel.promoter.navmodel.operation.promoteAll
import com.bumble.appyx.navmodel.promoter.transitionhandler.rememberPromoterTransitionHandler
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

@ExperimentalUnitApi
class PromoterTeaserNode(
    buildContext: BuildContext,
    private val promoter: Promoter<NavTarget> = Promoter(),
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = promoter
) {

    init {
        lifecycle.coroutineScope.launchWhenCreated {
//            repeat(4) {
            delay(1500)
            promoter.addFirst(NavTarget.Michaelle)
            promoter.promoteAll()
            promoter.addFirst(NavTarget.Lara)
            promoter.promoteAll()
            promoter.addFirst(NavTarget.Victoria)
            promoter.promoteAll()
            promoter.addFirst(NavTarget.Jack)
            promoter.promoteAll()
//            }
            delay(500)
//            repeat(4) {
//                delay(1500)
//                promoter.addFirst(NavTarget.Child((it + 5) * 100))
//                promoter.promoteAll()
//            }
            promoter.addFirst(NavTarget.Michaelle)
            promoter.promoteAll()
            promoter.addFirst(NavTarget.Lara)
            delay(1500)
            promoter.promoteAll()
            delay(1500)
            promoter.addFirst(NavTarget.Victoria)
            promoter.promoteAll()
            delay(1500)
            promoter.addFirst(NavTarget.Jack)
            promoter.promoteAll()
            delay(1500)
            promoter.addFirst(NavTarget.Kane)
            promoter.promoteAll()
            delay(1500)
            finish()
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
        val childSize = remember { 100.dp }
        Children(
            modifier = Modifier.fillMaxSize().background(color = Color.Black),
            navModel = promoter,
            transitionHandler = rememberPromoterTransitionHandler(childSize) {
                spring(stiffness = Spring.StiffnessVeryLow / 4)
            }
        ) {
            children<NavTarget> { child ->
                child(Modifier.size(childSize))
            }
        }
    }
}

