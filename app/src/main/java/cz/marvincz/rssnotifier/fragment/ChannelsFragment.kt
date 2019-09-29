package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.observe
import com.google.android.material.tabs.TabLayoutMediator
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.fragment_channels.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChannelsFragment : BaseFragment<ChannelsViewModel>() {
    override val viewModel: ChannelsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_channels, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val channelAdapter = ChannelAdapter(viewLifecycleOwner, childFragmentManager, viewModel.channels)
        pager.adapter = channelAdapter

        TabLayoutMediator(tabs, pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            channelAdapter.items.getOrNull(position)?.let { channel ->
                tab.text = channel.title
            }
        }).attach()

        handleEmptyState()

        swipe.setOnRefreshListener { viewModel.refreshAll(true) }
        fab.setOnClickListener { viewModel.addNew() }
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
        }
        return false
    }

    private fun handleEmptyState() {
        viewModel.channels.observe(this) {
            if (it.isEmpty()) {
                flipper.displayedChild = FLIP_EMPTY
                swipe.isEnabled = false
            } else {
                flipper.displayedChild = FLIP_TABS
                swipe.isEnabled = true
            }
        }
    }

    override fun setLoading(loading: Boolean) {
        swipe.isRefreshing = loading
    }

    companion object {
        private const val FLIP_TABS = 0
        private const val FLIP_EMPTY = 1
    }
}