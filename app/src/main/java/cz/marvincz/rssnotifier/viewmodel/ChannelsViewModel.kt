package cz.marvincz.rssnotifier.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import cz.marvincz.rssnotifier.extension.combine
import cz.marvincz.rssnotifier.extension.showSeen
import cz.marvincz.rssnotifier.extension.toggleShowSeen
import cz.marvincz.rssnotifier.extension.validIndex
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val repository: Repository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    val channels = repository.getChannels()

    private val _selectedChannelIndex = MutableLiveData(0)
    val selectedChannelIndex: LiveData<Int> get() = _selectedChannelIndex

    private val channelUrl = channels.combine(selectedChannelIndex) { channels, index ->
        val validIndex = channels.validIndex(index)
        if (validIndex != index) _selectedChannelIndex.value = validIndex
        channels[validIndex].accessUrl
    }
    val items = channelUrl.switchMap { channelUrl -> repository.getItems(channelUrl) }

    val showSeen = dataStore.showSeen

    val isRefreshing = mutableStateOf(false)

    init {
        refreshAll(forced = false)
    }

    fun onChannelSelected(index: Int) {
        _selectedChannelIndex.value = index
    }

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

    fun toggleShowSeen() {
        viewModelScope.launch {
            dataStore.toggleShowSeen()
        }
    }

    fun markAllRead() {
        viewModelScope.launch {
            channelUrl.value?.let { repository.markAllRead(it) }
        }
    }
}