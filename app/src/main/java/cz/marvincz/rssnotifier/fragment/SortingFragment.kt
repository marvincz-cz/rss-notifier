package cz.marvincz.rssnotifier.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.adapter.SortingAdapter
import cz.marvincz.rssnotifier.fragment.base.BaseFragment
import cz.marvincz.rssnotifier.model.RssChannel
import cz.marvincz.rssnotifier.viewmodel.SortingViewModel
import kotlinx.android.synthetic.main.fragment_sorting.*
import org.koin.android.viewmodel.ext.android.viewModel

class SortingFragment : BaseFragment<SortingViewModel>() {
    override val viewModel: SortingViewModel by viewModel()

    lateinit var adapter: SortingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sorting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dragCallback = DragCallback {
            viewModel.saveChannelOrder(adapter.items)
        }
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(list)

        adapter = SortingAdapter(viewLifecycleOwner, viewModel.channels, object : SortingAdapter.Listener {
            override fun onDelete(channel: RssChannel) {
                viewModel.delete(channel)
            }

            override fun drag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })
        dragCallback.moveListener = adapter

        list.adapter = adapter
        list.itemAnimator = DefaultItemAnimator()
    }

    class DragCallback(private val onMoveFinished: () -> Unit) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0) {
        var moveListener: MoveListener? = null

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            moveListener?.onMoved(viewHolder.adapterPosition, target.adapterPosition)
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