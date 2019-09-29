package cz.marvincz.rssnotifier.viewmodel

import androidx.lifecycle.ViewModel
import cz.marvincz.rssnotifier.background.WorkScheduler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(private val workScheduler: WorkScheduler) : ViewModel() {
    fun rescheduleWork() {
        GlobalScope.launch {
            delay(1_000)
            workScheduler.scheduleWork()
        }
    }

}
