package cz.marvincz.rssnotifier.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.component.IconType

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
private fun OneLinePreview() {
    ListItem(text = { Text("403: Hello World") },
        trailing = {
            Action(
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
            modifier = Modifier
                .size(dimensionResource(id = type.size)),
            painter = painterResource(id = res),
            tint = colorResource(id = R.color.icon_on_surface),
            contentDescription = stringResource(id = description)
        )
}

@Composable
fun Action(
    @DrawableRes res: Int,
    @StringRes description: Int = 0,
    action: () -> Unit
) {
    Icon(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.icon_clickable))
            .clickable(onClick = action)
            .padding(dimensionResource(id = R.dimen.icon_clickable_padding)),
        painter = painterResource(id = res),
        tint = colorResource(id = R.color.icon_on_surface),
        contentDescription = stringResource(id = description)
    )
}