package cz.marvincz.rssnotifier.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.util.PreferenceUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.net.URL

class Channels2ViewModel : ViewModel(), KoinComponent {
    private val repository: Repository = get()
    val channels = repository.getChannels()

    val selectedChannelIndex = mutableStateOf(0)

    val items = channels.switchMap { channels ->
        repository.getItems(channels[selectedChannelIndex.value].accessUrl)
    }

    val showSeen = PreferenceUtil.observeShowSeen()

    val isRefreshing = mutableStateOf(false)

    val addChannelShown = mutableStateOf(false)
    val addChannelUrl = mutableStateOf("")

    fun toggle(item: RssItem) {
        viewModelScope.launch {
            repository.updateItem(item.copy(seen = !item.seen))
        }
    }

    fun read(item: RssItem) {
        viewModelScope.launch {
            repository.updateItem(item.copy(seen = true))
        }
    }

    init {
        refreshAll(forced = false)
    }

    fun refreshAll() {
        isRefreshing.value = true
        refreshAll(true)
    }

    private fun refreshAll(forced: Boolean) {
        viewModelScope.launch {
            repository.refreshAll(forced)
            isRefreshing.value = false
        }
    }

    fun showAddChannel() {
        addChannelUrl.value = ""
        addChannelShown.value = true
    }

    fun dismissAddChannel() {
        addChannelUrl.value = ""
        addChannelShown.value = false
    }

    fun confirmAddChannel() {
        saveChannel(addChannelUrl.value)
        addChannelUrl.value = ""
        addChannelShown.value = false
    }

    private fun saveChannel(url: String) {
        url.takeIf { isValid(it) }
            ?.let {
                viewModelScope.launch { repository.download(it, false) }.invokeOnCompletion { cause ->
                    if (cause is CancellationException) {
                        // TODO Invalid RSS
                    }
                }
            } ?: run {} // TODO invalid URI
    }

    private fun isValid(url: String) = runCatching {
        URL(url).protocol in acceptedProtocols
    }.getOrDefault(false)

    companion object {
        private val acceptedProtocols = listOf("http", "https")
    }
}