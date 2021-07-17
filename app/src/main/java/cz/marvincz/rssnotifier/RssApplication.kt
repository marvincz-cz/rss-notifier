package cz.marvincz.rssnotifier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import cz.marvincz.rssnotifier.viewmodel.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class RssApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@RssApplication)
            modules(
                module
            )

            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NEW_UPDATES"
    }
}

private val module = module {
    single { Database.get(androidContext()) }
    single { Repository(get()) }
    single { WorkScheduler(androidContext()) }
    viewModel { ChannelsViewModel(get()) }
    viewModel { (channelUrl: String) -> ItemsViewModel(get(), channelUrl) }
    viewModel { AddChannelViewModel(get()) }
    viewModel { SortingViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { Channels2ViewModel(get()) }
    viewModel { ManageChannelsViewModel(get()) }
}
