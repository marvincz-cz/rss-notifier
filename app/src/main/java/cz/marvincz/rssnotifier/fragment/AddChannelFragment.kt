package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.databinding.FragmentAddChannelBinding
import cz.marvincz.rssnotifier.fragment.base.BaseDialogFragment
import cz.marvincz.rssnotifier.viewmodel.AddChannelViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddChannelFragment : BaseDialogFragment<AddChannelViewModel>() {
    override val viewModel: AddChannelViewModel by viewModel()

    private var _binding: FragmentAddChannelBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonOk.setOnClickListener { viewModel.submit(binding.url.text) }
        binding.buttonCancel.setOnClickListener { dismiss() }
        binding.url.doOnTextChanged { _, _, _, _ -> binding.urlLayout.error = null }
    }

    override fun handleViewCommand(command: ViewSpecificCommand): Boolean {
        return when (command) {
            AddChannelViewModel.Invalid.URI -> {
                binding.urlLayout.error = getString(R.string.validation_url)
                true
            }
            AddChannelViewModel.Invalid.RSS -> {
                binding.urlLayout.error = getString(R.string.validation_rss)
                true
            }
            else -> false
        }
    }

    override fun setLoading(loading: Boolean) {
        super.setLoading(loading)
        binding.url.isEnabled = !loading
        binding.buttonOk.isEnabled = !loading
        binding.buttonCancel.isEnabled = !loading
    }
}