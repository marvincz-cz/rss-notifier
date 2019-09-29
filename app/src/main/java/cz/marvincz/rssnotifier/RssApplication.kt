package cz.marvincz.rssnotifier

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import cz.marvincz.rssnotifier.background.Worker
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import cz.marvincz.rssnotifier.viewmodel.AddChannelViewModel
import cz.marvincz.rssnotifier.viewmodel.ChannelsViewModel
import cz.marvincz.rssnotifier.viewmodel.ItemsViewModel
import cz.marvincz.rssnotifier.viewmodel.SortingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

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
                        viewModel { SortingViewModel(get()) }
                    }
            )

            createNotificationChannel()
            scheduleWork()
        }

        instance = this
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

    private fun scheduleWork() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workRequest = PeriodicWorkRequestBuilder<Worker>(2, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(30, TimeUnit.MINUTES)
                .addTag(WORK_TAG)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.cancelAllWorkByTag(WORK_TAG)
        workManager.enqueue(workRequest)
    }

    companion object {
        lateinit var instance: RssApplication
            private set

        const val NOTIFICATION_CHANNEL_ID = "NEW_UPDATES"
        const val WORK_TAG = "PERIODIC_CHECK"
    }
}
