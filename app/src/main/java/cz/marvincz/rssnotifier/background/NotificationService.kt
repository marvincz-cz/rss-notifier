package cz.marvincz.rssnotifier.background

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.marvincz.rssnotifier.BuildConfig
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class NotificationService : IntentService("NotificationService") {
    private val repository: Repository by inject()

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            intent.data?.let { uri ->
                startActivity(Intent(Intent.ACTION_VIEW, uri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        intent?.getStringExtra(ITEM_ID)?.let { itemId ->
            runBlocking {
                repository.markRead(itemId)
            }
        }

        if (intent?.hasExtra(NOTIFICATION_ID) == true) {
            intent.getIntExtra(NOTIFICATION_ID, 0)
                    .let { notificationId ->
                        with(NotificationManagerCompat.from(applicationContext)) {
                            cancel(notificationId)
                        }
                    }
        }
    }

    companion object {
        const val ITEM_ID = BuildConfig.APPLICATION_ID + ".ITEM_ID"
        const val NOTIFICATION_ID = BuildConfig.APPLICATION_ID + ".NOTIFICATION_ID"
    }
}