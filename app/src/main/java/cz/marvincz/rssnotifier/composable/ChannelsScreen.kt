package cz.marvincz.rssnotifier.composable

import android.text.Html
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.util.InitialList
import cz.marvincz.rssnotifier.viewmodel.Channels2ViewModel
import java.time.ZonedDateTime

@Composable
fun ChannelsScreen(navController: NavController, onGoToItem: (String) -> Unit) {
    val viewModel: Channels2ViewModel = viewModel()
    val channels: List<RssChannel> by viewModel.channels.observeAsState(InitialList())
    val (selectedChannelIndex, onChannelSelected) = viewModel.selectedChannelIndex
    val items: List<RssItem> by viewModel.items.observeAsState(InitialList())
    val showSeen: Boolean by viewModel.showSeen.observeAsState(initial = true)
    val isRefreshing = viewModel.isRefreshing.value

    ChannelsScreen(
        channels = channels,
        selectedChannelIndex = selectedChannelIndex,
        onChannelSelected = onChannelSelected,
        items = items,
        onItemOpen = { item ->
            viewModel.read(item)
            item.link?.let { onGoToItem(it) }
        },
        onItemToggleSeen = { viewModel.toggle(it) },
        showSeen = showSeen,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshAll() }
    )
}

@Composable
private fun ChannelsScreen(
    channels: List<RssChannel>,
    selectedChannelIndex: Int,
    onChannelSelected: (Int) -> Unit,
    items: List<RssItem>,
    onItemOpen: (RssItem) -> Unit,
    onItemToggleSeen: (RssItem) -> Unit,
    showSeen: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    MaterialTheme {
        Surface {
            when {
                channels is InitialList || items is InitialList -> LoadingList()
                channels.isEmpty() -> EmptyText(R.string.no_channels_message)
                else -> Column {
                    ChannelTabs(channels, selectedChannelIndex, onChannelSelected)
                    ItemsList(
                        items,
                        onItemOpen,
                        onItemToggleSeen,
                        showSeen,
                        isRefreshing,
                        onRefresh
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelTabs(
    channels: List<RssChannel>,
    selectedChannelIndex: Int,
    onChannelSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedChannelIndex,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        channels.forEachIndexed { index, channel ->
            key(channel.accessUrl) {
                Tab(
                    selected = index == selectedChannelIndex,
                    onClick = { onChannelSelected(index) }
                ) {
                    Text(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding)),
                        text = channel.title,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemsList(
    items: List<RssItem>,
    onItemOpen: (RssItem) -> Unit,
    onItemToggleSeen: (RssItem) -> Unit,
    showSeen: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val shownItems = if (showSeen) items else items.filter { !it.seen }

    if (shownItems.isNotEmpty()) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = onRefresh
        ) {
            LazyColumn {
                items(shownItems, key = { it.id }) { item ->
                    val icon = if (item.seen) R.drawable.ic_check else R.drawable.ic_eye
                    val description =
                        if (item.seen) R.string.action_mark_unread else R.string.action_mark_read

                    ListItem(
                        modifier = Modifier.clickable { onItemOpen(item) },
                        text = { Text(stripHtml(item.title) ?: item.id) },
                        secondaryText = { Text(stripHtml(item.description) ?: "") },
                        trailing = {
                            ActionIcon(
                                icon,
                                description
                            ) { onItemToggleSeen(item) }
                        }
                    )
                }
            }
        }
    } else {
        val emptyText = if (items.isEmpty()) R.string.empty_no_items else R.string.empty_no_unseen
        EmptyText(emptyText)
    }
}

@Composable
private fun LoadingList() {
    Column {
        ScrollableTabRow(selectedTabIndex = 0, backgroundColor = MaterialTheme.colors.surface) {
            Tab(selected = true, onClick = {}) {
                ShimmerItem(
                    Modifier
                        .padding(all = dimensionResource(id = R.dimen.dimen_1))
                        .size(
                            width = dimensionResource(id = R.dimen.dimen_16),
                            height = dimensionResource(id = R.dimen.dimen_3)
                        )
                )
            }
        }
        repeat(6) {
            LoadingItem()
        }
    }
}

@Composable
private fun EmptyText(@StringRes stringRes: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.default_padding),
                vertical = dimensionResource(id = R.dimen.dimen_6)
            ),
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        text = stringResource(id = stringRes)
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun LoadingItem() {
    ListItem(
        modifier = Modifier.padding(bottom = 8.dp),
        text = {
            ShimmerItem(
                Modifier
                    .height(height = dimensionResource(id = R.dimen.dimen_3))
                    .fillMaxWidth(0.75f)
            )
        },
        trailing = {
            ShimmerItem(
                Modifier
                    .size(dimensionResource(id = R.dimen.icon_clickable))
                    .padding(dimensionResource(id = R.dimen.icon_clickable_padding))
            )
        }
    )
}

@Preview
@Composable
private fun Preview() {
    ChannelsScreen(
        channels = mockChannels,
        selectedChannelIndex = 0,
        onChannelSelected = {},
        items = mockItems,
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = true,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
private fun PreviewInitial() {
    ChannelsScreen(
        channels = InitialList(),
        selectedChannelIndex = 0,
        onChannelSelected = {},
        items = InitialList(),
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = true,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
private fun PreviewNoChannel() {
    ChannelsScreen(
        channels = emptyList(),
        selectedChannelIndex = 0,
        onChannelSelected = {},
        items = emptyList(),
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = true,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
private fun PreviewNoItems() {
    ChannelsScreen(
        channels = mockChannels,
        selectedChannelIndex = 1,
        onChannelSelected = {},
        items = emptyList(),
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = false,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
private fun PreviewNoUnseenItems() {
    ChannelsScreen(
        channels = mockChannels,
        selectedChannelIndex = 0,
        onChannelSelected = {},
        items = mockItems.map { it.copy(seen = true) },
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = false,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
private fun PreviewItemsListShowSeen() {
    ItemsList(
        items = mockItems,
        onItemOpen = {},
        onItemToggleSeen = {},
        showSeen = false,
        isRefreshing = false,
        onRefresh = {}
    )
}

@Preview
@Composable
fun PreviewLoadingItem() {
    Surface {
        LoadingItem()
    }
}

private val mockChannels
    get() = listOf(
        RssChannel("URL", null, "Webcomic", null, 0, ZonedDateTime.now()),
        RssChannel("URL2", null, "News Site", null, 0, ZonedDateTime.now()),
    )

private val mockItems
    get() = listOf(
        RssItem("0", null, null, "Title 1", "An article", false),
        RssItem("1", null, null, "Title 2", "Another article", true),
        RssItem("2", null, null, "Different Title", null, false),
        RssItem("3", null, null, "So Cool", "An article", true),
        RssItem("4", null, null, "Wonderful", "An article", true),
    )

private fun stripHtml(html: String?): String? {
    if (html == null) {
        return null
    }
    val text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString()
    return text.replace(OBJECT_PLACEHOLDER_CHARACTER, "")
        .trim { it <= ' ' }
        .replace("\n\n", "\n")
}

private const val OBJECT_PLACEHOLDER_CHARACTER = "￼"