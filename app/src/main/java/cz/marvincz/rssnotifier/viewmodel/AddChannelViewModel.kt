package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import kotlinx.coroutines.launch
import java.net.URL

class AddChannelViewModel(val repository: Repository) : BaseViewModel() {
    fun submit(url: CharSequence?) {
        url?.toString()?.takeIf { isValid(it) }
                ?.let {
                    viewModelScope.launch {
                        viewCommand(ViewCommand.Loading(true))
                        repository.download(it, false)
                        viewCommand(ViewCommand.Dismiss)
                    }
                } ?: viewCommand(ViewCommand.Specific(Invalid))
    }

    private fun isValid(url: String) = runCatching {
        URL(url).protocol in acceptedProtocols
    }.getOrDefault(false)

    companion object {
        private val acceptedProtocols = listOf("http", "https")
    }

    object Invalid : ViewSpecificCommand {
        override val oneTime = true
    }
}