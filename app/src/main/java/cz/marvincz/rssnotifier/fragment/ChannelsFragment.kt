package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.fragment_channels.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChannelsFragment : Fragment() {
    private val viewModel: ChannelsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_channels, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pager.adapter = ChannelAdapter(this, parentFragmentManager, viewModel.channels)
    }
}