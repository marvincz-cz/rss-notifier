package cz.marvincz.rssnotifier.viewmodel

import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel

class SortingViewModel(repository: Repository) : BaseViewModel() {
    val channels = repository.getChannels()

    fun delete(channel: RssChannel) {

    }
}