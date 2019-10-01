package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SortingViewModel(private val repository: Repository) : BaseViewModel() {
    val channels = repository.getChannelsOneTime()

    fun delete(channel: RssChannel) {
        viewCommand(ViewCommand.Loading(true))
        viewModelScope.launch {
            repository.deleteChannel(channel)
            viewCommand(ViewCommand.Specific(Remove(channel)))
            viewCommand(ViewCommand.Loading(false))
        }
    }

    fun saveChannelOrder(channels: List<RssChannel>) {
        viewCommand(ViewCommand.Loading(true))
        GlobalScope.launch(Dispatchers.Main) {
            repository.saveChannelOrder(channels)
            viewCommand(ViewCommand.Loading(false))
        }
    }

    data class Remove(val channel: RssChannel): ViewSpecificCommand {
        override val oneTime: Boolean
            get() = true
    }
}