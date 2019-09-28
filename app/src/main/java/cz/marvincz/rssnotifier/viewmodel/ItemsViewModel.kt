package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.util.PreferenceUtil
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class ItemsViewModel(private val repository: Repository, private val channelUrl: String) : BaseViewModel() {
    val items = PreferenceUtil.observeShowSeen().switchMap { showSeen ->
        repository.getItems(channelUrl, showSeen)
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

    fun markAllRead() {
        viewModelScope.launch {
            repository.markAllRead(channelUrl)
        }
    }

    fun toggleShowSeen() {
        PreferenceUtil.toggleShowSeen()
    }
}