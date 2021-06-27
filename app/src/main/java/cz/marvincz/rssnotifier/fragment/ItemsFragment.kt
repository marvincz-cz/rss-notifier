package cz.marvincz.rssnotifier.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.ItemAdapter
import cz.marvincz.rssnotifier.databinding.FragmentItemsBinding
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.viewmodel.ItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ItemsFragment : BaseFragment<ItemsViewModel>() {
    val channelUrl: String by lazy { requireArguments().getString(ARG_CHANNEL)!! }
    override val viewModel: ItemsViewModel by viewModel { parametersOf(channelUrl) }

    private var _binding: FragmentItemsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.adapter = ItemAdapter(viewLifecycleOwner, viewModel.items, object : ItemAdapter.ItemListCallBack {
            override fun toggle(item: RssItem) {
                viewModel.toggle(item)
            }

            override fun open(item: RssItem) {
                viewModel.read(item)
                item.link?.let { goToLink(it) }
            }

        })
        binding.list.itemAnimator = DefaultItemAnimator()

        handleEmptyState()
    }

    private fun handleEmptyState() {
        viewModel.items.observe(this.viewLifecycleOwner) {
            if (it.isEmpty())
                binding.flipper.displayedChild = FLIP_EMPTY
            else
                binding.flipper.displayedChild = FLIP_LIST
        }

        viewModel.showSeen.observe(this.viewLifecycleOwner) { showSeen ->
            if (showSeen)
                binding.emptyMessage.setText(R.string.empty_no_items)
            else
                binding.emptyMessage.setText(R.string.empty_no_unseen)
        }
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
            R.id.action_show_seen -> {
                viewModel.toggleShowSeen()
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
         * @return A new instance of fragment ItemsFragment.
         */
        fun newInstance(channelUrl: String) = ItemsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CHANNEL, channelUrl)
            }
        }

        private const val FLIP_LIST = 0
        private const val FLIP_EMPTY = 1
    }
}
