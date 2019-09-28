package cz.marvincz.rssnotifier.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ItemAdapter
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.viewmodel.ItemsViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RssItemFragment : BaseFragment<ItemsViewModel>() {
    val channelUrl: String by lazy { arguments!!.getString(ARG_CHANNEL)!! }
    override val viewModel: ItemsViewModel by viewModel { parametersOf(channelUrl) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list.adapter = ItemAdapter(viewLifecycleOwner, viewModel.items, object : ItemAdapter.ItemListCallBack {
            override fun toggle(item: RssItem) {
                viewModel.toggle(item)
            }

            override fun open(item: RssItem) {
                viewModel.read(item)
                item.link?.let { goToLink(it) }
            }

        })
        list.itemAnimator = DefaultItemAnimator()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_all_seen -> {
                viewModel.markAllRead()
                return true
            }
        }
        return false
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
