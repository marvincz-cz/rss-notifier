package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cz.marvincz.rssnotifier.adapter.SortingAdapter
import cz.marvincz.rssnotifier.databinding.FragmentSortingBinding
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.viewmodel.SortingViewModel
import cz.marvincz.rssnotifier.viewmodel.base.ViewSpecificCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

class SortingFragment : BaseFragment<SortingViewModel>() {
    override val viewModel: SortingViewModel by viewModel()

    private var _binding: FragmentSortingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var adapter: SortingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragCallback = DragCallback {
            viewModel.saveChannelOrder(adapter.items)
        }
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(binding.list)

        adapter = SortingAdapter(viewLifecycleOwner, viewModel.channels, object : SortingAdapter.Listener {
            override fun onDelete(channel: RssChannel) {
                viewModel.delete(channel)
            }

            override fun drag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })
        dragCallback.moveListener = adapter

        binding.list.adapter = adapter
        binding.list.itemAnimator = DefaultItemAnimator()
    }

    override fun handleViewCommand(command: ViewSpecificCommand): Boolean {
        if (command is SortingViewModel.Remove) {
            adapter.remove(command.channel)
            return true
        } else return super.handleViewCommand(command)
    }

    class DragCallback(private val onMoveFinished: () -> Unit) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0) {
        var moveListener: MoveListener? = null

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            moveListener?.onMoved(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

        override fun isLongPressDragEnabled() = false

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            onMoveFinished()
        }
    }

    interface MoveListener {
        fun onMoved(position: Int, toPosition: Int)
    }
}