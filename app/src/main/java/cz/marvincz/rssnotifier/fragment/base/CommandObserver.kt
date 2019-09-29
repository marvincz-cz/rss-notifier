package cz.marvincz.rssnotifier.fragment.base

import androidx.annotation.CallSuper
import androidx.navigation.NavController
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand

/**
 * Common parent to Fragments and DialogFragments for common implementation of handling commands from the ViewModel
 */
interface CommandObserver<T : BaseViewModel> {
    val viewModel: T

    /**
     * Default implementation for UI commands
     */
    @CallSuper
    fun handleDefaultViewCommand(viewCommand: ViewCommand): Boolean {
        return when (viewCommand) {
            is ViewCommand.Noop -> true
            is ViewCommand.Navigation -> {
                navController().navigate(viewCommand.directions)
                true
            }
            is ViewCommand.Loading -> {
                setLoading(viewCommand.loading)
                true
            }
            is ViewCommand.Specific -> handleViewCommand(viewCommand.command)
            else -> false
        }
    }

    /**
     * React to view-specific commands from the ViewModel.
     *
     * For implementation of default commands see [handleDefaultViewCommand]
     */
    fun handleViewCommand(command: ViewSpecificCommand): Boolean = false

    /**
     * Helper method
     */
    fun navController(): NavController

    /**
     * Set loading state of the UI
     */
    fun setLoading(loading: Boolean) = Unit
}