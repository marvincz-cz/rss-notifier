package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.launch

class ManageChannelsViewModel(private val repository: Repository) : ViewModel() {
    val channels = repository.getChannels()

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
}
