package com.bumble.appyx.components.spotlight.ui.sliderscale

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.combineState
import com.bumble.appyx.interactions.AppyxLogger
import com.bumble.appyx.interactions.core.ui.context.UiContext
import com.bumble.appyx.interactions.core.ui.math.cutOffCenterSigned
import com.bumble.appyx.interactions.core.ui.math.scaleUpTo
import com.bumble.appyx.interactions.core.ui.property.impl.Alpha
import com.bumble.appyx.interactions.core.ui.property.impl.Height
import com.bumble.appyx.interactions.core.ui.property.impl.Position
import com.bumble.appyx.interactions.core.ui.property.impl.RotationY
import com.bumble.appyx.interactions.core.ui.property.impl.RoundedCorners
import com.bumble.appyx.interactions.core.ui.property.impl.Width
import com.bumble.appyx.interactions.core.ui.property.impl.ZIndex
import com.bumble.appyx.interactions.core.ui.state.MutableUiStateSpecs
import com.bumble.appyx.mapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

@MutableUiStateSpecs
class TargetUiState(
    val rotationY: RotationY.Target,
    val width: Width.Target,
    val height: Height.Target,
    val position: Position.Target,
    val roundedCorners: RoundedCorners.Target,
    val zIndex: ZIndex.Target,
    val alpha: Alpha.Target
) {
    /**
     * Take item's own position in the list of elements into account
     */
    constructor(
        base: TargetUiState,
        positionInList: StateFlow<Int>,
        elementWidth: Dp,
        zIndex: ZIndex.Target,
        roundedCorners: RoundedCorners.Target
    ) : this(
        position = Position.Target(
            base.position.value.copy(
                x = (positionInList.value * elementWidth.value * 0.65f).dp
            )
        ),
        alpha = base.alpha,
        width = base.width,
        height = base.height,
        zIndex = zIndex,
        rotationY = base.rotationY,
        roundedCorners = roundedCorners
    )

    /**
     * Takes the dynamically changing scroll into account when calculating values.
     *
     * TODO support RTL and Orientation.Vertical
     */
    fun toMutableState(
        uiContext: UiContext,
        scrollX: StateFlow<Float>,
        positionInList: StateFlow<Int> = MutableStateFlow(0),
        elementWidth: Dp
    ): MutableUiState {
        return MutableUiState(
            uiContext = uiContext,
            position = Position(
                uiContext, position,
                scrollX.mapState(uiContext.coroutineScope) {
                    DpOffset((it * elementWidth.value).dp * 0.65f, 0.dp)
                },
            ),
            height = Height(uiContext, height,
                displacement = scrollX.combineState(
                    positionInList,
                    uiContext.coroutineScope
                ) { scrollX, positionInList ->
//                    AppyxLogger.d("STATE", "new scroll for $this is $it")
                    (abs(positionInList - scrollX) - 0.15f).coerceIn(0f, 0.1f)
                }
            ),
            alpha = Alpha(uiContext, alpha),
            width = Width(uiContext, width,
                displacement = scrollX.combineState(
                    positionInList,
                    uiContext.coroutineScope
                ) { scrollX, positionInList ->
                    (abs(positionInList - scrollX) - 0.15f).coerceIn(0f, 0.1f)
                }
            ),
            rotationY = RotationY(uiContext, rotationY,
//                displacement = scrollX.combineState(
//                    positionInList,
//                    uiContext.coroutineScope
//                ) { scrollX, positionInList ->
//                    scaleUpTo(cutOffCenterSigned(positionInList.toFloat(), scrollX, 0.15f), 15f, 1f)
//                }
            ),
            zIndex = ZIndex(uiContext, zIndex,
                displacement = scrollX.combineState(
                    positionInList,
                    uiContext.coroutineScope
                ) { scrollX, positionInList ->
                    (abs(positionInList - scrollX))
                }),
            roundedCorners = RoundedCorners(uiContext, roundedCorners)
        )
    }
}
