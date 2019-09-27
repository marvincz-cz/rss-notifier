package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.SortingAdapter
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.viewmodel.SortingViewModel
import kotlinx.android.synthetic.main.fragment_sorting.*
import org.koin.android.viewmodel.ext.android.viewModel

class SortingFragment : BaseFragment<SortingViewModel>() {
    override val viewModel: SortingViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sorting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list.adapter = SortingAdapter(viewLifecycleOwner, viewModel.channels, object : SortingAdapter.Listener {
            override fun onDelete(channel: RssChannel) {
                viewModel.delete(channel)
            }
        })
        list.layoutManager = LinearLayoutManager(context)
        list.itemAnimator = DefaultItemAnimator()
    }
}