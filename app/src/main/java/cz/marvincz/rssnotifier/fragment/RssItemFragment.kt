package cz.marvincz.rssnotifier.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ItemAdapter
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.viewmodel.ItemsViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class RssItemFragment : Fragment() {
    private lateinit var viewModel: ItemsViewModel

    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelUrl = arguments!!.getString(ARG_CHANNEL)!!
        viewModel = getViewModel { parametersOf(channelUrl) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ItemAdapter(this, viewModel.items, object : ItemAdapter.ItemListCallBack {
            override fun toggle(item: RssItem) {
                viewModel.toggle(item)
            }

            override fun open(item: RssItem) {
                viewModel.read(item)
                goToLink(item.link)
            }

        })
    }

    private fun goToLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    companion object {
        private const val ARG_CHANNEL = "channel_param"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param channelUrl RSS Channel ID
         * @return A new instance of fragment RssItemFragment.
         */
        fun newInstance(channelUrl: String) = RssItemFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CHANNEL, channelUrl)
            }
        }
    }
}
