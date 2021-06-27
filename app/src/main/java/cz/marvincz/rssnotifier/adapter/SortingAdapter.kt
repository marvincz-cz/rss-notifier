package cz.marvincz.rssnotifier.adapter

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.component.OneLine
import cz.marvincz.rssnotifier.extension.swap
import cz.marvincz.rssnotifier.extension.tintedDrawable
import cz.marvincz.rssnotifier.fragment.SortingFragment
import cz.marvincz.rssnotifier.model.RssChannel

class SortingAdapter(lifecycleOwner: LifecycleOwner, data: LiveData<List<RssChannel>>, private val listener: Listener) : BaseAdapter<RssChannel>(lifecycleOwner, data), SortingFragment.MoveListener {
    override fun inflateView(parent: ViewGroup, viewType: Int): View = OneLine(parent.context)

    override fun onBind(itemView: View, position: Int, item: RssChannel, holder: ViewHolder) {
        require(itemView is OneLine)

        itemView.text = item.title
        itemView.drawable = itemView.tintedDrawable(R.drawable.ic_reorder, R.color.on_surface_high)
        itemView.setActionDrawable(R.drawable.ic_delete)
        itemView.setActionDescription(R.string.action_delete)

        itemView.setActionListener { listener.onDelete(item) }
        itemView.icon.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) listener.drag(holder)
            false
        }
    }

    override fun onMoved(position: Int, toPosition: Int) {
        val channels = items.toMutableList()

        if (position < toPosition) {
            for (i in position until toPosition) channels.swap(i, i + 1)
        } else {
            for (i in position downTo toPosition + 1) channels.swap(i, i - 1)
        }
        items = channels.toList()
        notifyItemMoved(position, toPosition)
    }

    fun remove(item: RssChannel) {
        if (items.contains(item)) {
            val position = items.indexOf(item)
            items = items.minus(item)
            notifyItemRemoved(position)
        }
    }

    interface Listener {
        fun onDelete(channel: RssChannel)
        fun drag(viewHolder: RecyclerView.ViewHolder)
    }
}