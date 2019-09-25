package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.launch

class ChannelsViewModel(private val repository: Repository) : ViewModel() {
    val channels = repository.getChannels()

    fun download(currentChannel: RssChannel) {
        viewModelScope.launch {
            repository.download(currentChannel.accessUrl)
        }
    }
}