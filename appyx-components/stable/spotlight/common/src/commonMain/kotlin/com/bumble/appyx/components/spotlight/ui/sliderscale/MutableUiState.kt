//// Generated file ... DO NOT EDIT!
//package com.bumble.appyx.components.spotlight.ui.sliderscale
//
//import androidx.compose.animation.core.SpringSpec
//import androidx.compose.animation.core.spring
//import androidx.compose.ui.Modifier
//import com.bumble.appyx.interactions.core.ui.context.UiContext
//import com.bumble.appyx.interactions.core.ui.property.impl.Height
//import com.bumble.appyx.interactions.core.ui.property.impl.Position
//import com.bumble.appyx.interactions.core.ui.property.impl.RoundedCorners
//import com.bumble.appyx.interactions.core.ui.property.impl.Width
//import com.bumble.appyx.interactions.core.ui.property.impl.ZIndex
//import com.bumble.appyx.interactions.core.ui.state.BaseMutableUiState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.awaitAll
//import kotlinx.coroutines.launch
//
//public class MutableUiState(
//  uiContext: UiContext,
//  public val width: Width,
//  public val height: Height,
//  public val position: Position,
//  public val roundedCorners: RoundedCorners,
//  public val zIndex: ZIndex,
//) : BaseMutableUiState<TargetUiState>(
//  uiContext = uiContext,
//  motionProperties = listOf(width, height, position, roundedCorners, zIndex),
//) {
//  public override val modifier: Modifier = Modifier
//    .then(width.modifier)
//    .then(height.modifier)
//    .then(position.modifier)
//    .then(roundedCorners.modifier)
//    .then(zIndex.modifier)
//
//
//  public override suspend fun animateTo(
//    scope: CoroutineScope,
//    target: TargetUiState,
//    springSpec: SpringSpec<Float>,
//  ): Unit {
//      listOf(
//        scope.async {
//          width.animateTo(
//            target.width.value,
//            spring(springSpec.dampingRatio, springSpec.stiffness),
//          )
//        },
//        scope.async {
//          height.animateTo(
//            target.height.value,
//            spring(springSpec.dampingRatio, springSpec.stiffness),
//          )
//        },
//        scope.async {
//          position.animateTo(
//            target.position.value,
//            spring(springSpec.dampingRatio, springSpec.stiffness),
//          )
//        },
//        scope.async {
//          roundedCorners.animateTo(
//            target.roundedCorners.value,
//            spring(springSpec.dampingRatio, springSpec.stiffness),
//          )
//        },
//        scope.async {
//          zIndex.animateTo(
//            target.zIndex.value,
//            spring(springSpec.dampingRatio, springSpec.stiffness),
//          )
//        },
//      ).awaitAll()
//  }
//
//  public override suspend fun snapTo(target: TargetUiState): Unit {
//    width.snapTo(target.width.value)
//    height.snapTo(target.height.value)
//    position.snapTo(target.position.value)
//    roundedCorners.snapTo(target.roundedCorners.value)
//    zIndex.snapTo(target.zIndex.value)
//  }
//
//  public override fun lerpTo(
//    scope: CoroutineScope,
//    start: TargetUiState,
//    end: TargetUiState,
//    fraction: Float,
//  ): Unit {
//    scope.launch {
//      width.lerpTo(start.width, end.width, fraction)
//      height.lerpTo(start.height, end.height, fraction)
//      position.lerpTo(start.position, end.position, fraction)
//      roundedCorners.lerpTo(start.roundedCorners, end.roundedCorners, fraction)
//      zIndex.lerpTo(start.zIndex, end.zIndex, fraction)
//    }
//  }
//}
//
//public fun TargetUiState.toMutableState(uiContext: UiContext): MutableUiState = MutableUiState(
//  uiContext = uiContext,
//  width = com.bumble.appyx.interactions.core.ui.`property`.`impl`.Width(uiContext, width),
//  height = com.bumble.appyx.interactions.core.ui.`property`.`impl`.Height(uiContext, height),
//  position = com.bumble.appyx.interactions.core.ui.`property`.`impl`.Position(uiContext, position),
//  roundedCorners = com.bumble.appyx.interactions.core.ui.`property`.`impl`.RoundedCorners(uiContext,
//      roundedCorners),
//  zIndex = com.bumble.appyx.interactions.core.ui.`property`.`impl`.ZIndex(uiContext, zIndex),
//)
