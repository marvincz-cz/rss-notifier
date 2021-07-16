package cz.marvincz.rssnotifier.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.component.IconType

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
private fun OneLinePreview() {
    ListItem(text = { Text("403: Hello World") },
        trailing = {
            ActionIcon(
                R.drawable.ic_eye,
                R.string.action_mark_read
            ) {}
        })
}

@Composable
fun Icon(
    type: IconType = IconType.SMALL,
    @DrawableRes res: Int = 0,
    @StringRes description: Int = 0
) {
    if (type != IconType.NONE)
        Icon(
            modifier = Modifier.size(dimensionResource(id = type.size)),
            painter = painterResource(id = res),
            tint = iconOnSurface(isSystemInDarkTheme()),
            contentDescription = stringResource(id = description)
        )
}

@Composable
fun ActionIcon(
    @DrawableRes res: Int,
    @StringRes description: Int = 0,
    action: () -> Unit
) {
    Icon(
        modifier = Modifier
            .size(iconClickable)
            .clickable(onClick = action)
            .padding(iconClickablePadding),
        painter = painterResource(id = res),
        tint = iconOnSurface(isSystemInDarkTheme()),
        contentDescription = stringResource(id = description)
    )
}

@Composable
fun TopBarMenu(items: List<MenuItem>) {
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val toggleMenu = { setShowMenu(!showMenu) }

    IconButton(onClick = toggleMenu) {
        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = stringResource(R.string.menu)
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = toggleMenu,
        offset = DpOffset(0.dp, -dimen(4)),
    ) {
        items.forEach { item ->
            DropdownMenuItem(onClick = { toggleMenu.invoke(); item.onClick.invoke() }) {
                Text(text = item.text)
            }
        }
    }
}

data class MenuItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        /*
         Specify animation positions,
         initial Values 0F means it
         starts from 0 position
        */
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(


            // Tween Animates between values over specified [durationMillis]
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )
    val shimmerColorShades = listOf(
        Color.LightGray.copy(0.7f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.7f)
    )

    Spacer(modifier = modifier.background(
        brush = Brush.linearGradient(
            colors = shimmerColorShades,
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )
    ))
}

@Preview
@Composable
private fun PreviewShimmerItem() {
    Surface {
        ShimmerItem(
            Modifier
                .padding(36.dp)
                .size(width = 128.dp, height = 48.dp))
    }
}