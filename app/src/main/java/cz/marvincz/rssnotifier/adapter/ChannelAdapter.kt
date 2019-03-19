package cz.marvincz.rssnotifier.adapter

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cz.marvincz.rssnotifier.fragment.RssItemFragment
import cz.marvincz.rssnotifier.model.RssChannel

class ChannelAdapter(activity: AppCompatActivity, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var currentFragment: RssItemFragment? = null

    var data: List<RssChannel> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) =
            RssItemFragment.newInstance(data[position].accessUrl)

    override fun getCount() = data.size

    override fun getPageTitle(position: Int) = data[position].title.orEmpty()

    override fun setPrimaryItem(container: ViewGroup, position: Int, fragment: Any) {
        super.setPrimaryItem(container, position, fragment)
        currentFragment = fragment as RssItemFragment
    }
}
