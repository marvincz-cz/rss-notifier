package cz.marvincz.rssnotifier.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import java.net.URL

class ManageChannelsViewModel(private val repository: Repository, addChannel: Boolean?) : ViewModel() {
    val channels = repository.getChannels()

    val addChannelShown = mutableStateOf(addChannel ?: false)
    val addChannelUrl = mutableStateOf("")
    val addChannelError = mutableStateOf(0)

    private var lock = false

    fun deleteChannel(channel: RssChannel) {
        viewModelScope.launch {
            repository.deleteChannel(channel)
        }
    }

    fun onDragged(positions: Map<String, Float>) {
        val list = channels.value ?: return
        if (!positions.keys.containsAll(list.map { it.accessUrl })) return

        val newOrder = list.sortedBy { positions[it.accessUrl] }

        if (list != newOrder) {
            viewModelScope.launch {
                repository.saveChannelOrder(newOrder)
            }
        }
    }

    fun showAddChannel() {
        addChannelUrl.value = ""
        addChannelError.value = 0
        addChannelShown.value = true
        lock = false
    }

    fun dismissAddChannel() {
        addChannelUrl.value = ""
        addChannelError.value = 0
        addChannelShown.value = false
        lock = false
    }

    fun confirmAddChannel() {
        saveChannel(addChannelUrl.value)
    }

    private fun saveChannel(url: String) {
        if (lock) return
        lock = true

        url.takeIf { isValid(it) }
            ?.let {
                viewModelScope.launch {
                    repository.download(it, false)
                    dismissAddChannel()
                }
                    .invokeOnCompletion { cause ->
                        if (cause is CancellationException) {
                            addChannelError.value = R.string.validation_rss
                            lock = false
                        }
                    }
            } ?: run {
            addChannelError.value = R.string.validation_url
            lock = false
        }
    }

    private fun isValid(urlString: String) = runCatching {
        val url = URL(urlString)
        url.openConnection()
        url.protocol in acceptedProtocols
    }.getOrDefault(false)

    companion object {
        private val acceptedProtocols = listOf("http", "https")
    }
}
