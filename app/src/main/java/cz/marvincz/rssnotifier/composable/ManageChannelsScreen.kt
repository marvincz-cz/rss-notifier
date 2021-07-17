package cz.marvincz.rssnotifier.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.util.InitialList
import cz.marvincz.rssnotifier.util.stripHtml
import cz.marvincz.rssnotifier.viewmodel.ManageChannelsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ManageChannelsScreen(navController: NavController) {
    val viewModel: ManageChannelsViewModel = getViewModel()

    val channels: List<RssChannel> by viewModel.channels.observeAsState(InitialList())

    ManageChannelsScreen(
        onBack = { navController.popBackStack() },
        channels = channels,
        onDeleteChannel = { viewModel.deleteChannel(it) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageChannelsScreen(
    onBack: () -> Unit,
    channels: List<RssChannel>,
    onDeleteChannel: (RssChannel) -> Unit
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
                LazyColumn {
                    items(channels, key = { it.accessUrl }) { channel ->
                        ListItem(
                            text = { Text(stripHtml(channel.title) ?: channel.accessUrl) },
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

