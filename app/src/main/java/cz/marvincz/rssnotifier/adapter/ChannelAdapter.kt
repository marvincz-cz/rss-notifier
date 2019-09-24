package cz.marvincz.rssnotifier.adapter

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import cz.marvincz.rssnotifier.fragment.RssItemFragment
import cz.marvincz.rssnotifier.model.RssChannel

class ChannelAdapter(lifecycleOwner: LifecycleOwner, fragmentManager: FragmentManager, data: LiveData<List<RssChannel>>) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var items: List<RssChannel> = listOf()
    private var currentChannel: RssChannel? = null

    init {
        data.observe(lifecycleOwner, Observer {
            items = it
            notifyDataSetChanged()
        })
    }

    override fun getItem(position: Int) =
            RssItemFragment.newInstance(items[position].accessUrl)

    override fun getCount() = items.size

    override fun getPageTitle(position: Int) = items[position].title.orEmpty()

    override fun setPrimaryItem(container: ViewGroup, position: Int, fragment: Any) {
        super.setPrimaryItem(container, position, fragment)
        currentChannel = items[position]
    }
}
