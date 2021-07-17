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
}