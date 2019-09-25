package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import kotlinx.coroutines.launch

class ChannelsViewModel(private val repository: Repository) : BaseViewModel() {
    val channels = repository.getChannels()

    init {
        viewModelScope.launch {
            repository.refreshAll()
        }
    }

    fun reload(currentChannel: RssChannel) {
        viewModelScope.launch {
            repository.download(currentChannel.accessUrl, true)
            viewCommand(ViewCommand.Loading(false))
        }
    }
}