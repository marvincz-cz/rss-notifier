package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.fragment.base.BaseDialogFragment
import cz.marvincz.rssnotifier.viewmodel.AddChannelViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import kotlinx.android.synthetic.main.fragment_add_channel.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddChannelFragment : BaseDialogFragment<AddChannelViewModel>() {
    override val viewModel: AddChannelViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_add_channel, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_ok.setOnClickListener { viewModel.submit(url.text) }
        button_cancel.setOnClickListener { dismiss() }
        url.doOnTextChanged { _, _, _, _ -> url_layout.error = null }
    }

    override fun handleViewCommand(command: ViewSpecificCommand): Boolean {
        return when (command) {
            AddChannelViewModel.Invalid.URI -> {
                url_layout.error = getString(R.string.validation_url)
                true
            }
            AddChannelViewModel.Invalid.RSS -> {
                url_layout.error = getString(R.string.validation_rss)
                true
            }
            else -> false
        }
    }

    override fun setLoading(loading: Boolean) {
        super.setLoading(loading)
        url.isEnabled = !loading
        button_ok.isEnabled = !loading
        button_cancel.isEnabled = !loading
    }
}