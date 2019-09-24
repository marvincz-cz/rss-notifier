package cz.marvincz.rssnotifier.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
    lifecycleOwner: LifecycleOwner,
    data: LiveData<List<T>>
) : RecyclerView.Adapter<ViewHolder>() {
    private var items: List<T> = emptyList()

    init {
        data.observe(lifecycleOwner, Observer {
            items = it
            notifyDataSetChanged()
        })
    }

    override fun getItemCount() = items.size

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateView(parent, viewType))
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.itemView, position, items[position])
    }

    abstract fun inflateView(parent: ViewGroup, viewType: Int): View

    abstract fun onBind(itemView: View, position: Int, item: T)
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)