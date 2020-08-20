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
import cz.marvincz.rssnotifier.model.RssItem
import cz.marvincz.rssnotifier.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.random.Random

class Worker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params), KoinComponent {
    private val repository: Repository by inject()
    override suspend fun doWork(): Result {
        val newItems = repository.refreshAll(forced = BuildConfig.DEBUG)

        newItems.flatMap { (channel, list) -> list.map { channel to it } }
                .forEach { (channel, item) ->
                    val notificationId = Random.nextInt()

                    // TODO: Preference - Open browser or open the app on the channel tab
                    val pendingIntent = if (item.link != null) createOpenIntent(item)
                    else NavDeepLinkBuilder(applicationContext)
                            .setGraph(R.navigation.nav_graph)
                            .setDestination(R.id.channelsFragment)
                            .setArguments(ChannelsFragmentArgs(channel.accessUrl).toBundle())
                            .createPendingIntent()

                    val markReadPendingIntent = createMarkReadIntent(item, notificationId)

                    val notificationBuilder = NotificationCompat.Builder(applicationContext, RssApplication.NOTIFICATION_CHANNEL_ID)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                            .setSmallIcon(R.drawable.ic_rss_feed)
                            .setContentTitle(channel.title)
                            .setContentText(item.title)
                            .setContentIntent(pendingIntent)
                            .addAction(R.drawable.ic_check, applicationContext.getString(R.string.action_mark_read), markReadPendingIntent)
                            .setAutoCancel(true)

                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(notificationId, notificationBuilder.build())
                    }
                }

        return Result.success()
    }

    private fun createOpenIntent(item: RssItem): PendingIntent {
        val intent = Intent(applicationContext, NotificationService::class.java)
                .setAction(Intent.ACTION_VIEW)
                .setData(Uri.parse(item.link))
                .putExtra(NotificationService.ITEM_ID, item.id)

        return PendingIntent.getService(applicationContext, REQUEST_OPEN, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private fun createMarkReadIntent(item: RssItem, notificationId: Int): PendingIntent {
        val intent = Intent(applicationContext, NotificationService::class.java)
                .setData(Uri.parse(item.link))
                .putExtra(NotificationService.ITEM_ID, item.id)
                .putExtra(NotificationService.NOTIFICATION_ID, notificationId)

        return PendingIntent.getService(applicationContext, REQUEST_MARK_READ, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    companion object {
        private const val REQUEST_OPEN = 1
        private const val REQUEST_MARK_READ = 2
    }
}