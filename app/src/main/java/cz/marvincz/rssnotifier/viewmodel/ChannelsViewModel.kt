package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.fragment.ChannelsFragmentDirections
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import kotlinx.coroutines.launch

class ChannelsViewModel(private val repository: Repository) : BaseViewModel() {
    val channels = repository.getChannels()

    init {
        refreshAll(forced = false)
    }

    fun refreshAll(forced: Boolean) {
        viewModelScope.launch {
            repository.refreshAll(forced)
            viewCommand(ViewCommand.Loading(false))
        }
    }

    fun addNew() {
        navigate(ChannelsFragmentDirections.actionAddChannel())
    }

    fun sortItems() {
        navigate(ChannelsFragmentDirections.actionSortChannels())
    }

    fun settings() {
        navigate(ChannelsFragmentDirections.actionSettings())
    }
}