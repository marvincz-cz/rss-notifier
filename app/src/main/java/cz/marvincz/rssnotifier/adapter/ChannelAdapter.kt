package cz.marvincz.rssnotifier.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.marvincz.rssnotifier.fragment.ItemsFragment
import cz.marvincz.rssnotifier.model.RssChannel

class ChannelAdapter(lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager, data: LiveData<List<RssChannel>>) : FragmentStateAdapter(fragmentManager, lifecycleOwner.lifecycle) {
    var items: List<RssChannel> = listOf()
        private set

    init {
        data.observe(lifecycleOwner) {
            items = it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = items.size

    override fun createFragment(position: Int) = ItemsFragment.newInstance(items[position].accessUrl)

    override fun getItemId(position: Int) = items[position].id

    override fun containsItem(itemId: Long) = items
            .any { it.id == itemId }

    private val RssChannel.id: Long
        get() = accessUrl.hashCode().toLong()
}
