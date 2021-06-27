package cz.marvincz.rssnotifier.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import cz.marvincz.rssnotifier.BuildConfig
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent


class NotificationBroadcastReceiver : BroadcastReceiver() {
    private val repository: Repository by KoinJavaComponent.inject(Repository::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        val itemId = intent.getStringExtra(ITEM_ID) ?: return

        if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.let { uri ->
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, uri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        runBlocking {
            repository.markRead(itemId)
        }

        if (intent.hasExtra(NOTIFICATION_ID)) {
            intent.getIntExtra(NOTIFICATION_ID, 0)
                .let { notificationId ->
                    with(NotificationManagerCompat.from(context)) {
                        cancel(notificationId)
                    }
                }
        }
    }

    companion object {
        const val ITEM_ID = BuildConfig.APPLICATION_ID + ".ITEM_ID"
        const val NOTIFICATION_ID = BuildConfig.APPLICATION_ID + ".NOTIFICATION_ID"

        const val ACTION_MARK_READ = BuildConfig.APPLICATION_ID + ".ACTION_MARK_READ"
    }
}