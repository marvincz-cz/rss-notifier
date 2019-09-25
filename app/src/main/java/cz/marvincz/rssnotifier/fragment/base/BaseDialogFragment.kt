package cz.marvincz.rssnotifier.fragment.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import cz.marvincz.rssnotifier.viewmodel.base.BaseViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewCommand
import cz.marvincz.rssnotifier.extension.setLayout

/**
 * Parent to all page DialogFragments. Handles commands from the ViewModel
 */
abstract class BaseDialogFragment<T : BaseViewModel> : DialogFragment(),
        CommandObserver<T> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("EXPERIMENTAL_API_USAGE")
        viewModel.viewCommands.observe(this) {viewCommand ->
            // All commands must be handled by the view. The view fails when it receives a command that it can't handle.
            require(handleDefaultViewCommand(viewCommand)) { "View ${this::class} received unhandled command: $viewCommand" }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // stretch the dialog horizontally to fill the screen (with margins)
        setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Dialog handles the extra [Dismiss][ViewCommand.Dismiss] [ViewCommand]
     */
    @Override
    override fun handleDefaultViewCommand(viewCommand: ViewCommand): Boolean {
        return super.handleDefaultViewCommand(viewCommand)
                || when (viewCommand) {
            is ViewCommand.Dismiss -> {
                dismiss()
                true
            }
            else -> false
        }
    }

    final override fun navController(): NavController = findNavController()
}