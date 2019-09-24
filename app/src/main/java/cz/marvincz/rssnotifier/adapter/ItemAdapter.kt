package cz.marvincz.rssnotifier.adapter

import android.content.Context
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.model.RssItem
import kotlinx.android.synthetic.main.item_two_line.view.*
import java.util.function.Consumer

class ItemAdapter(private val context: Context, var callback: (RssItem, Boolean) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var data: List<RssItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_two_line, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data.get(position)

        viewHolder.itemView.title.text = stripHtml(item.title)
        viewHolder.itemView.description.text = stripHtml(item.description)
        if (item.seen) {
            viewHolder.itemView.action_icon.setImageResource(R.drawable.ic_eye_closed)
            viewHolder.itemView.action_icon.contentDescription = context.getString(R.string.action_mark_unread)
        } else {
            viewHolder.itemView.action_icon.setImageResource(R.drawable.ic_eye)
            viewHolder.itemView.action_icon.contentDescription = context.getString(R.string.action_mark_read)
        }
        viewHolder.itemView.action_icon.visibility = View.VISIBLE
        viewHolder.itemView.action_area.visibility = View.VISIBLE
        viewHolder.itemView.action_area.setOnClickListener {
            item.seen = !item.seen
            notifyItemChanged(position)
            callback(item, false)
        }
        viewHolder.itemView.setOnClickListener {
            item.seen = true
            notifyItemChanged(position)
            callback(item, true)
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

private fun stripHtml(html: String?): String? {
    if (html == null) {
        return null
    }
    val text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString()
    return text.replace(OBJECT_PLACEHOLDER_CHARACTER, "")
            .trim { it <= ' ' }
            .replace("\n\n", "\n")
}

private const val OBJECT_PLACEHOLDER_CHARACTER = "￼"