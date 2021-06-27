package cz.marvincz.rssnotifier.fragment.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel

/**
 * Parent to all page Fragments. Handles commands from the ViewModel
 */
abstract class BaseFragment<T : BaseViewModel> : Fragment(),
        CommandObserver<T> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("EXPERIMENTAL_API_USAGE")
        viewModel.viewCommands.observe(this) {viewCommand ->
            // All commands must be handled by the view. The view fails when it receives a command that it can't handle.
            require(handleDefaultViewCommand(viewCommand)) { "View ${this::class} received unhandled command: $viewCommand" }
        }
    }

    final override fun navController(): NavController = findNavController()
}