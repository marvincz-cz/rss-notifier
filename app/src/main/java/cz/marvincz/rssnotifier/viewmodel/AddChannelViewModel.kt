package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.util.PreferenceUtil
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.net.URL

class AddChannelViewModel(val repository: Repository) : BaseViewModel() {
    fun submit(url: CharSequence?) {
        url?.toString()?.takeIf { isValid(it) }
                ?.let {
                    viewModelScope.launch {
                        viewCommand(ViewCommand.Loading(true))
                        repository.download(it, false)
                        addToOrder(it)
                        viewCommand(ViewCommand.Dismiss)
                    }.invokeOnCompletion { cause ->
                        if (cause is CancellationException) {
                            viewCommand(ViewCommand.Loading(false))
                            viewCommand(ViewCommand.Specific(Invalid.RSS))
                        }
                    }
                } ?: viewCommand(ViewCommand.Specific(Invalid.URI))
    }

    private fun isValid(url: String) = runCatching {
        URL(url).protocol in acceptedProtocols
    }.getOrDefault(false)

    private fun addToOrder(url: String) {
        val newOrder = PreferenceUtil.getChannelOrder()
                .toMutableList().apply {
                    add(url)
                }
        PreferenceUtil.setChannelOrder(newOrder)
    }

    companion object {
        private val acceptedProtocols = listOf("http", "https")
    }

    enum class Invalid(override val oneTime: Boolean = false) : ViewSpecificCommand {
        URI, RSS
    }
}