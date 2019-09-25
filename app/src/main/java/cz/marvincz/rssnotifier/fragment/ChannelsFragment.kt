package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import kotlinx.android.synthetic.main.fragment_channels.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChannelsFragment : BaseFragment<ChannelsViewModel>() {
    override val viewModel: ChannelsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_channels, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val channelAdapter = ChannelAdapter(this, parentFragmentManager, viewModel.channels)
        pager.adapter = channelAdapter

        handleEmptyState()

        swipe.setOnRefreshListener {
            channelAdapter.currentChannel?.let {
                viewModel.reload(it)
            } ?: swipe.setRefreshing(false)
        }
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
        private const val FLIP_TABS = 0;
        private const val FLIP_EMPTY = 1;
    }
}