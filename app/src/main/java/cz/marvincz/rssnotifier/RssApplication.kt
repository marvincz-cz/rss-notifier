package cz.marvincz.rssnotifier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.extension.dataStore
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import cz.marvincz.rssnotifier.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class RssApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RssApplication)
            modules(
                module
            )

            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
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

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NEW_UPDATES"
    }
}

private val module = module {
    single { androidContext().dataStore }
    single { Database.get(androidContext()) }
    single { Repository(get()) }
    single { WorkScheduler(androidContext(), get()) }
    viewModel { ChannelsViewModel(get(), get()) }
    viewModel { parameters -> ManageChannelsViewModel(get(), parameters.get()) }
    viewModel { SettingsViewModel(get(), get()) }
}
