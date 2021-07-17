package cz.marvincz.rssnotifier.background

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.marvincz.rssnotifier.extension.wifiOnly
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import java.time.Duration
import java.util.concurrent.TimeUnit

class WorkScheduler(private val context: Context, private val dataStore: DataStore<Preferences>) {
    suspend fun scheduleWork() {
        val wifiOnly = dataStore.wifiOnly.first()

        val constraints = Constraints.Builder()
            .apply {
                if (wifiOnly)
                    setRequiredNetworkType(NetworkType.UNMETERED)
                else
                    setRequiredNetworkType(NetworkType.CONNECTED)
            }
            .build()

        val workRequest =
            PeriodicWorkRequestBuilder<Worker>(CHECK_INTERVAL.toMinutes(), TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(INITIAL_DELAY.toMinutes(), TimeUnit.MINUTES)
                .addTag(WORK_TAG)
                .build()

        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(WORK_TAG)
        workManager.enqueue(workRequest)
        Log.i("WorkScheduler", "Background work scheduled")
    }

    companion object {
        const val WORK_TAG = "PERIODIC_CHECK"
        private val CHECK_INTERVAL = Duration.ofHours(1)
        private val INITIAL_DELAY = Duration.ofMinutes(10)
    }
}