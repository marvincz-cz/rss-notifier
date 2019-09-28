package cz.marvincz.rssnotifier.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
        lifecycleOwner: LifecycleOwner,
        data: LiveData<List<T>>
) : RecyclerView.Adapter<ViewHolder>() {
    var items: List<T> = emptyList()
        protected set

    init {
        data.observe(lifecycleOwner) {
            items = it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = items.size

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateView(parent, viewType))
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.itemView, position, items[position], holder)
    }

    abstract fun inflateView(parent: ViewGroup, viewType: Int): View

    abstract fun onBind(itemView: View, position: Int, item: T, holder: ViewHolder)
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)