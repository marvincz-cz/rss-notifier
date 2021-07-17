package cz.marvincz.rssnotifier.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.marvincz.rssnotifier.background.WorkScheduler
import cz.marvincz.rssnotifier.extension.setWifiOnly
import cz.marvincz.rssnotifier.extension.wifiOnly
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: DataStore<Preferences>, private val workScheduler: WorkScheduler) : ViewModel() {
    val wifiOnly = dataStore.wifiOnly

    fun setWifiOnly(value: Boolean) {
        viewModelScope.launch {
            dataStore.setWifiOnly(value)
            workScheduler.scheduleWork()
        }
    }
}