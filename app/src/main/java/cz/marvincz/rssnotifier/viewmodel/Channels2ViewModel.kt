package cz.marvincz.rssnotifier.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.util.PreferenceUtil
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Channels2ViewModel : ViewModel(), KoinComponent {
    private val repository: Repository = get()
    val channels = repository.getChannels()

    val selectedChannelIndex = mutableStateOf(0)

    val items = channels.switchMap { channels ->
        repository.getItems(channels[selectedChannelIndex.value].accessUrl)
    }

    val showSeen = PreferenceUtil.observeShowSeen()

    val isRefreshing = mutableStateOf(false)

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
}