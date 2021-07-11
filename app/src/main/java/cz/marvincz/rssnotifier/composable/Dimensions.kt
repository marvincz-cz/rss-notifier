package cz.marvincz.rssnotifier.composable

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun dimen(step: Int): Dp {
    require(step >= 0)
    return (8 * step).dp
}

fun dimenIcon(step: Int): Dp {
    require(step >= 0)
    return (4 * step).dp
}

val defaultPadding = dimen(2)
val viewPadding = dimen(1)

val iconClickable = dimenIcon(12)
val iconClickablePadding = dimenIcon(3)