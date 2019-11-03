package cz.marvincz.rssnotifier.background

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cz.marvincz.rssnotifier.util.PreferenceUtil
import org.threeten.bp.Duration
import java.util.concurrent.TimeUnit

class WorkScheduler(private val context: Context) {
    fun scheduleWork() {
        val constraints = Constraints.Builder()
                .apply {
                    if (PreferenceUtil.isWifiOnly())
                        setRequiredNetworkType(NetworkType.UNMETERED)
                    else
                        setRequiredNetworkType(NetworkType.CONNECTED)
                }
                .build()
        val workRequest = PeriodicWorkRequestBuilder<Worker>(CHECK_INTERVAL.toMinutes(), TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInitialDelay(CHECK_INTERVAL.dividedBy(2)
                        .toMinutes(), TimeUnit.MINUTES)
                .addTag(WORK_TAG)
                .build()
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(WORK_TAG)
        workManager.enqueue(workRequest)
    }

    companion object {
        const val WORK_TAG = "PERIODIC_CHECK"
        private val CHECK_INTERVAL = Duration.ofHours(1)
    }
}