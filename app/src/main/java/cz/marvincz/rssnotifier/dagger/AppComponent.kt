package cz.marvincz.rssnotifier.dagger

import javax.inject.Singleton

import cz.marvincz.rssnotifier.activity.MainActivity
import cz.marvincz.rssnotifier.adapter.ItemAdapter
import cz.marvincz.rssnotifier.fragment.RssItemFragment
import cz.marvincz.rssnotifier.repository.Repository
import dagger.Component

@Singleton
@Component(modules = [DaggerModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(repository: Repository)
    fun inject(itemFragment: RssItemFragment)
    fun inject(itemAdapter: ItemAdapter)
}
