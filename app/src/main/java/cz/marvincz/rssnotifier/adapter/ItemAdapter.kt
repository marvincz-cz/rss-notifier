package cz.marvincz.rssnotifier.adapter

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.component.TwoLine
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.util.stripHtml

class ItemAdapter(lifecycleOwner: LifecycleOwner, data: LiveData<List<RssItem>>, private val callBack: ItemListCallBack) : BaseAdapter<RssItem>(lifecycleOwner, data) {
    override fun inflateView(parent: ViewGroup, viewType: Int) = TwoLine(parent.context)

    override fun onBind(itemView: View, position: Int, item: RssItem, holder: ViewHolder) {
        require(itemView is TwoLine)

        itemView.text = stripHtml(item.title)
        itemView.secondaryText = stripHtml(item.description)
        if (item.seen) {
            itemView.setActionDrawable(R.drawable.ic_check)
            itemView.setActionDescription(R.string.action_mark_unread)
            itemView.emphasis = false
        } else {
            itemView.setActionDrawable(R.drawable.ic_eye)
            itemView.setActionDescription(R.string.action_mark_read)
            itemView.emphasis = true
        }
        itemView.setActionListener { callBack.toggle(item) }
        itemView.setOnClickListener { callBack.open(item) }
    }

    interface ItemListCallBack {
        fun toggle(item: RssItem)
        fun open(item: RssItem)
    }
}