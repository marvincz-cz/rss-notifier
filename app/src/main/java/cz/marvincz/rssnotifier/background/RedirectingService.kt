package cz.marvincz.rssnotifier.background

import android.app.IntentService
import android.content.Intent
import cz.marvincz.rssnotifier.BuildConfig
import cz.marvincz.rssnotifier.repository.Repository
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class RedirectingService : IntentService("RedirectingService") {
    private val repository: Repository by inject()

    override fun onHandleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            startActivity(Intent(Intent.ACTION_VIEW, uri)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        intent?.getStringExtra(ITEM_ID)?.let { itemId ->
            runBlocking {
                repository.markRead(itemId)
            }
        }
    }

    companion object {
        const val ITEM_ID = BuildConfig.APPLICATION_ID + ".ITEM_ID"
    }
}