package cz.marvincz.rssnotifier.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Parent to all ViewModels, handles commands for the View
 */
open class BaseViewModel(initialCommand: ViewCommand? = null) : ViewModel() {
    private lateinit var sendCommand: (ViewCommand) -> Unit

    /**
     * A stream of commands for the associated View.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val viewCommands = callbackFlow {
        initialCommand?.let { trySend(it) }
        sendCommand = { trySend(it) }
        awaitClose()
    }.asLiveData(viewModelScope.coroutineContext)

    /**
     * Send a command to the associated View
     */
    protected fun viewCommand(viewCommand: ViewCommand) {
        sendCommand(viewCommand)

        // If the command is one-time only, follow it with Noop immediately, so it isn't kept in the LiveData
        if (viewCommand.oneTime) sendCommand(ViewCommand.Noop)
    }

    /**
     * Send a navigation command to the associated View.
     *
     * Shorthand for [viewCommand] with [ViewCommand.Navigation].
     */
    protected fun navigate(directions: NavDirections) {
        viewCommand(ViewCommand.Navigation(directions))
    }
}