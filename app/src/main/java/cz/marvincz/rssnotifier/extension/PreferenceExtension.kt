package cz.marvincz.rssnotifier.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private const val DATA_STORE = "settings"

private val SHOW_SEEN = booleanPreferencesKey("show_seen")
private val WIFI_ONLY = booleanPreferencesKey("wifi_only")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE)

val DataStore<Preferences>.showSeen get() = data.map { preferences -> preferences[SHOW_SEEN] ?: true }
suspend fun DataStore<Preferences>.toggleShowSeen() = edit { preferences ->
    val currentValue = preferences[SHOW_SEEN] ?: true
    preferences[SHOW_SEEN] = !currentValue
}

val DataStore<Preferences>.wifiOnly get() = data.map { preferences -> preferences[WIFI_ONLY] ?: false }
suspend fun DataStore<Preferences>.setWifiOnly(value: Boolean) = edit { preferences ->
    preferences[WIFI_ONLY] = value
}