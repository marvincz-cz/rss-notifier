package cz.marvincz.rssnotifier.background

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.marvincz.rssnotifier.BuildConfig
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.RssApplication
import cz.marvincz.rssnotifier.fragment.ChannelsFragmentArgs
import cz.marvincz.rssnotifier.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

class Worker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params), KoinComponent {
    private val repository: Repository by inject()
    override suspend fun doWork(): Result {
        val newItems = repository.refreshAll(forced = BuildConfig.DEBUG)

        newItems.flatMap { (channel, list) -> list.map { channel to it } }
                .forEach { (channel, item) ->
                    val notificationId = item.id.hashCode()

                    // TODO: Preference - Open browser or open the app on the channel tab
                    val pendingIntent = if (item.link != null) {
                        val intent = Intent(applicationContext, RedirectingService::class.java)
                                .setData(Uri.parse(item.link))
                                .putExtra(RedirectingService.ITEM_ID, item.id)
                        PendingIntent.getService(applicationContext, 0, intent, 0)
                    } else {
                        NavDeepLinkBuilder(applicationContext)
                                .setGraph(R.navigation.nav_graph)
                                .setDestination(R.id.channelsFragment)
                                .setArguments(ChannelsFragmentArgs(channel.accessUrl).toBundle())
                                .createPendingIntent()
                    }

                    val builder = NotificationCompat.Builder(applicationContext, RssApplication.NOTIFICATION_CHANNEL_ID)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setSmallIcon(R.drawable.ic_rss_feed)
                            .setContentTitle(item.title)
                            .setContentText(channel.title)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)

                    with(NotificationManagerCompat.from(applicationContext)) {
                        // notificationId is a unique int for each notification that you must define
                        notify(notificationId, builder.build())
                    }
                }

        return Result.success()
    }
}