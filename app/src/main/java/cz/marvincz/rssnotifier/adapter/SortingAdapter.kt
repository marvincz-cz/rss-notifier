package cz.marvincz.rssnotifier.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.component.OneLine
import cz.marvincz.rssnotifier.extension.tintedDrawable
import cz.marvincz.rssnotifier.model.RssChannel

class SortingAdapter(lifecycleOwner: LifecycleOwner, data: LiveData<List<RssChannel>>, private val listener: Listener) : BaseAdapter<RssChannel>(lifecycleOwner, data) {
    override fun inflateView(parent: ViewGroup, viewType: Int): View = OneLine(parent.context)

    override fun onBind(itemView: View, position: Int, item: RssChannel) {
        require(itemView is OneLine)

        itemView.text = item.title
        itemView.drawable = itemView.tintedDrawable(R.drawable.ic_reorder, R.color.on_surface_high)
        itemView.setActionDrawable(R.drawable.ic_delete)
        itemView.setActionDescription(R.string.action_delete)

        itemView.setActionListener { listener.onDelete(item) }
    }

    interface Listener {
        fun onDelete(channel: RssChannel)
    }
}