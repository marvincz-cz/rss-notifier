package cz.marvincz.rssnotifier

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import cz.marvincz.rssnotifier.viewmodel.AddChannelViewModel
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import cz.marvincz.rssnotifier.viewmodel.ItemsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class RssApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@RssApplication)
            modules(
                    module {
                        single { Database.get(androidContext()) }
                        single { Repository(get()) }
                        viewModel { ChannelsViewModel(get()) }
                        viewModel { (channelUrl: String) -> ItemsViewModel(get(), channelUrl) }
                        viewModel { AddChannelViewModel(get()) }
                    }
            )
        }

        instance = this
    }

    companion object {
        lateinit var instance: RssApplication
            private set
    }
}
