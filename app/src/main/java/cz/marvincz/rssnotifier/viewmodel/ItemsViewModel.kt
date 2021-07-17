package cz.marvincz.rssnotifier.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.extension.showSeen
import cz.marvincz.rssnotifier.extension.toggleShowSeen
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class ItemsViewModel(private val repository: Repository, private val dataStore: DataStore<Preferences>, private val channelUrl: String) : BaseViewModel() {
    val showSeen = dataStore.showSeen.asLiveData(viewModelScope.coroutineContext)

    val items = showSeen.switchMap { showSeen ->
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
        viewModelScope.launch {
            dataStore.toggleShowSeen()
        }
    }
}