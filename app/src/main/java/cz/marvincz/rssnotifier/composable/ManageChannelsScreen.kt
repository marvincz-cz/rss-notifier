package cz.marvincz.rssnotifier.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.extension.copyAndPut
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.util.InitialList
import cz.marvincz.rssnotifier.util.stripHtml
import cz.marvincz.rssnotifier.viewmodel.ManageChannelsViewModel
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt

@Composable
fun ManageChannelsScreen(navController: NavController) {
    val viewModel: ManageChannelsViewModel = getViewModel()

    val channels: List<RssChannel> by viewModel.channels.observeAsState(InitialList())

    ManageChannelsScreen(
        onBack = navController::popBackStack,
        channels = channels,
        onDeleteChannel = viewModel::deleteChannel,
        onDragged = viewModel::onDragged
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageChannelsScreen(
    onBack: () -> Unit,
    channels: List<RssChannel>,
    onDeleteChannel: (RssChannel) -> Unit,
    onDragged: (Map<String, Float>) -> Unit
) {
    MaterialTheme(colors = colors(isSystemInDarkTheme())) {
        Surface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.fragment_sorting)) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.action_back)
                                )
                            }
                        }
                    )
                }
            ) {
                var positions by remember { mutableStateOf(mapOf<String, Float>()) }

                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(channels, key = { it.accessUrl }) { channel ->
                        var offset by remember { mutableStateOf(0f) }
                        val elevation = if (offset != 0f) DRAGGED_ELEVATION else 0f
                        val background = if (offset != 0f) elevatedBackground() else MaterialTheme.colors.surface

                        ListItem(
                            modifier = Modifier
                                .offset { IntOffset(0, offset.roundToInt()) }
                                .zIndex(elevation)
                                .shadow(elevation.dp)
                                .background(background)
                                .onGloballyPositioned { coordinates ->
                                    positions = positions.copyAndPut(
                                        channel.accessUrl,
                                        coordinates.localToRoot(Offset.Zero).y
                                    )
                                },
                            text = { Text(text = stripHtml(channel.title) ?: channel.accessUrl) },
                            icon = {
                                Icon(
                                    modifier = Modifier
                                        .size(iconClickable)
                                        .draggable(
                                            orientation = Orientation.Vertical,
                                            state = rememberDraggableState { delta ->
                                                offset += delta
                                            },
                                            onDragStopped = {
                                                onDragged.invoke(positions)
                                                offset = 0f
                                            }
                                        )
                                        .padding(iconClickablePadding),
                                    painter = painterResource(R.drawable.ic_reorder),
                                    contentDescription = stringResource(R.string.action_reorder)
                                )
                            },
                            trailing = {
                                ActionIcon(
                                    R.drawable.ic_delete,
                                    R.string.action_delete
                                ) { onDeleteChannel.invoke(channel) }
                            }
                        )
                    }
                }
            }
        }
    }
}

private const val DRAGGED_ELEVATION = 8f