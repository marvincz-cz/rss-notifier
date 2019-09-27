package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.fragment.ChannelsFragmentDirections
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.util.PreferenceUtil
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import kotlinx.coroutines.launch

class ChannelsViewModel(private val repository: Repository) : BaseViewModel() {
    val channels = repository.getChannels()
            .map { it.sortedBy { ch -> PreferenceUtil.getChannelOrder().indexOf(ch.accessUrl) } } // FIXME: Better comparator

    init {
        viewModelScope.launch {
            repository.refreshAll()
        }
    }

    fun reload(channel: RssChannel?) {
        viewModelScope.launch {
            channel?.let { repository.download(channel.accessUrl, true) }
            viewCommand(ViewCommand.Loading(false))
        }
    }

    fun addNew() {
        navigate(ChannelsFragmentDirections.actionAddChannel())
    }

    fun sortItems() {
        navigate(ChannelsFragmentDirections.actionSortChannels())
    }
}