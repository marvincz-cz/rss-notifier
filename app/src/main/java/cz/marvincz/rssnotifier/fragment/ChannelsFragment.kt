package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.*
import com.google.android.material.tabs.TabLayoutMediator
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import cz.marvincz.rssnotifier.databinding.FragmentChannelsBinding
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelsFragment : BaseFragment<ChannelsViewModel>() {
    override val viewModel: ChannelsViewModel by viewModel()

    private var _binding: FragmentChannelsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val channelAdapter = ChannelAdapter(viewLifecycleOwner, childFragmentManager, viewModel.channels)
        binding.pager.adapter = channelAdapter

        TabLayoutMediator(binding.tabs, binding.pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            channelAdapter.items.getOrNull(position)?.let { channel ->
                tab.text = channel.title
            }
        }).attach()

        handleEmptyState()

        binding.swipe.setOnRefreshListener { viewModel.refreshAll(true) }
        binding.fab.setOnClickListener { viewModel.addNew() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_channels, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_channels -> {
                viewModel.sortItems()
                return true
            }
            R.id.action_settings -> {
                viewModel.settings()
                return true
            }
        }
        return false
    }

    private fun handleEmptyState() {
        viewModel.channels.observe(this.viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.flipper.displayedChild = FLIP_EMPTY
                binding.swipe.isEnabled = false
            } else {
                binding.flipper.displayedChild = FLIP_TABS
                binding.swipe.isEnabled = true
            }
        }
    }

    override fun setLoading(loading: Boolean) {
        binding.swipe.isRefreshing = loading
    }

    companion object {
        private const val FLIP_TABS = 0
        private const val FLIP_EMPTY = 1
    }
}