package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.ViewModel
import cz.marvincz.rssnotifier.repository.Repository

class ChannelsViewModel(private val repository: Repository) : ViewModel() {
    val channels = repository.getChannels()
}