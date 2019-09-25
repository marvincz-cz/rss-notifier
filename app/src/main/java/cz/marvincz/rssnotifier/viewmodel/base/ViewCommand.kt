package cz.marvincz.rssnotifier.viewmodel.base

import androidx.navigation.NavDirections

/**
 * Commands sent from the ViewModel to the View
 */
sealed class ViewCommand(val oneTime: Boolean = false) {
    /**
     * No action
     */
    object Noop : ViewCommand()

    /**
     * Navigation to a different screen
     */
    data class Navigation(val directions: NavDirections) : ViewCommand(oneTime = true)

    /**
     * Loading indication
     */
    data class Loading(val loading: Boolean = true) : ViewCommand()

    /**
     * **DialogFragment only** - dismiss the dialog.
     */
    object Dismiss : ViewCommand()

    /**
     * Commands specific to the view. Wraps a [ViewSpecificCommand] subclass defined in the specific ViewMode
     */
    data class Specific(val command: ViewSpecificCommand) : ViewCommand(command.oneTime)
}

/**
 * View-specific commands, to be overridden for the specific screen
 */
interface ViewSpecificCommand {
    val oneTime: Boolean
}