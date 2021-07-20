package cz.marvincz.rssnotifier.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.extension.copyAndPut
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.util.stripHtml
import cz.marvincz.rssnotifier.viewmodel.ManageChannelsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@Composable
fun ManageChannelsScreen(navController: NavController, addChannel: Boolean?) {
    val viewModel: ManageChannelsViewModel = getViewModel(parameters = { parametersOf(addChannel) })

    val channels: List<RssChannel>? by viewModel.channels.observeAsState(null)
    val addChannelShown = viewModel.addChannelShown.value
    val (addChannelUrl, onAddChannelUrlChanged) = viewModel.addChannelUrl
    val addChannelError = viewModel.addChannelError.value

    ManageChannelsScreen(
        onBack = navController::popBackStack,
        channels = channels,
        onDeleteChannel = viewModel::deleteChannel,
        onDragged = viewModel::onDragged,
        addChannelShown = addChannelShown,
        onAddChannelShow = viewModel::showAddChannel,
        addChannelUrl = addChannelUrl,
        onAddChannelUrlChanged = onAddChannelUrlChanged,
        onAddChannelConfirm = viewModel::confirmAddChannel,
        onAddChannelCancel = viewModel::dismissAddChannel,
        addChannelError = addChannelError,
    )
}

@Composable
fun ManageChannelsScreen(
    onBack: () -> Unit,
    channels: List<RssChannel>?,
    onDeleteChannel: (RssChannel) -> Unit,
    onDragged: (Map<String, Float>) -> Unit,
    addChannelShown: Boolean,
    onAddChannelShow: () -> Unit,
    addChannelUrl: String,
    onAddChannelUrlChanged: (String) -> Unit,
    onAddChannelConfirm: () -> Unit,
    onAddChannelCancel: () -> Unit,
    addChannelError: Int,
) {
    MaterialTheme(colors = colors(isSystemInDarkTheme())) {
        Surface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.screen_manage_channels)) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.action_back)
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = onAddChannelShow) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = stringResource(R.string.add_channel_title)
                        )
                    }
                },
            ) {
                when {
                    channels == null -> LoadingList()
                    channels.isEmpty() -> EmptyText(stringRes = R.string.no_channels_message)
                    else -> DraggableList(channels, onDragged, onDeleteChannel)
                }

                if (addChannelShown)
                    AddChannel(
                        addChannelUrl,
                        onAddChannelUrlChanged,
                        onAddChannelConfirm,
                        onAddChannelCancel,
                        addChannelError,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DraggableList(
    channels: List<RssChannel>,
    onDragged: (Map<String, Float>) -> Unit,
    onDeleteChannel: (RssChannel) -> Unit
) {
    var positions by remember { mutableStateOf(mapOf<String, Float>()) }

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(channels, key = { it.accessUrl }) { channel ->
            var offset by remember { mutableStateOf(0f) }
            val elevation = if (offset != 0f) DRAGGED_ELEVATION else 0f
            val background =
                if (offset != 0f) elevatedBackground() else MaterialTheme.colors.surface

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
                text = {
                    Text(
                        text = stripHtml(channel.title) ?: channel.accessUrl,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
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
                                    onDragged(positions)
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
                    ) { onDeleteChannel(channel) }
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AddChannel(
    channelUrl: String,
    onUrlChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    addChannelError: Int,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = 0) {
        delay(100)
        focusRequester.requestFocus()
    }

    AlertDialog(
        modifier = Modifier.padding(horizontal = defaultPadding),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = { Text(text = stringResource(R.string.add_channel_title)) },
        onDismissRequest = onCancel,
        confirmButton = {
            Button(
                onClick = onConfirm
            ) { Text(text = stringResource(R.string.action_ok)) }
        },
        dismissButton = {
            Button(onClick = onCancel) { Text(text = stringResource(R.string.action_cancel)) }
        },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = channelUrl,
                    onValueChange = onUrlChanged,
                    placeholder = { Text(text = stringResource(R.string.channel_url_hint)) },
                    singleLine = true,
                    isError = addChannelError != 0,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onConfirm() })
                )
                if (addChannelError != 0) {
                    Text(
                        text = stringResource(id = addChannelError),
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    )
}

@Composable
private fun LoadingList() {
    Column {
        repeat(2) {
            LoadingItem()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun LoadingItem() {
    ListItem(
        icon = {
            ShimmerItem(
                Modifier
                    .size(iconClickable)
                    .padding(iconClickablePadding)
            )
        },
        modifier = Modifier.padding(bottom = viewPadding),
        text = {
            ShimmerItem(
                Modifier
                    .height(height = dimen(3))
                    .fillMaxWidth(0.6f)
            )
        },
        trailing = {
            ShimmerItem(
                Modifier
                    .size(iconClickable)
                    .padding(iconClickablePadding)
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    ManageChannelsScreen(
        onBack = {},
        channels = mockChannels,
        onDeleteChannel = {},
        onDragged = {},
        addChannelShown = false,
        onAddChannelShow = {},
        addChannelUrl = "",
        onAddChannelUrlChanged = {},
        onAddChannelConfirm = {},
        onAddChannelCancel = {},
        addChannelError = 0
    )
}

@Preview
@Composable
private fun PreviewEmpty() {
    ManageChannelsScreen(
        onBack = {},
        channels = emptyList(),
        onDeleteChannel = {},
        onDragged = {},
        addChannelShown = false,
        onAddChannelShow = {},
        addChannelUrl = "",
        onAddChannelUrlChanged = {},
        onAddChannelConfirm = {},
        onAddChannelCancel = {},
        addChannelError = 0
    )
}

@Preview
@Composable
private fun PreviewLoading() {
    ManageChannelsScreen(
        onBack = {},
        channels = null,
        onDeleteChannel = {},
        onDragged = {},
        addChannelShown = false,
        onAddChannelShow = {},
        addChannelUrl = "",
        onAddChannelUrlChanged = {},
        onAddChannelConfirm = {},
        onAddChannelCancel = {},
        addChannelError = 0
    )
}

private const val DRAGGED_ELEVATION = 8f