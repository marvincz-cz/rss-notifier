package cz.marvincz.rssnotifier.util

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import cz.marvincz.rssnotifier.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PreferenceUtil : KoinComponent {
    private val context: Context by inject()
    val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    private fun name(@StringRes prefKey: Int) = context.getString(prefKey)

    fun observeShowSeen(): LiveData<Boolean> = observe(R.string.preference_show_seen, true)

    fun toggleShowSeen() {
        val name = name(R.string.preference_show_seen)
        put(name, !get(name, true))
    }

    fun isWifiOnly(): Boolean = get(R.string.preference_wifi_only, false)

    /**
     * Generic getter for a preference value
     */
    private inline fun <reified T> get(@StringRes prefKey: Int, default: T): T = get(name(prefKey), default)

    /**
     * Generic getter for a preference value
     */
    private inline fun <reified T> get(name: String, default: T): T = with(preferences) {
        when (default) {
            is Int -> getInt(name, default) as T
            is Long -> getLong(name, default) as T
            is Float -> getFloat(name, default) as T
            is String -> getString(name, default) as T
            is String? -> getString(name, default) as T
            is Boolean -> getBoolean(name, default) as T
            else -> throw IllegalArgumentException("This type cannot be retrieved from Preferences")
        }
    }

    /**
     * Generic setter for a preference value
     */
    private fun <T> put(@StringRes prefKey: Int, value: T) = put(name(prefKey), value)

    /**
     * Generic setter for a preference value
     */
    private fun <T> put(name: String, value: T) = preferences.edit {
        when (value) {
            is Int -> putInt(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            is String -> putString(name, value)
            is String? -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            else -> throw IllegalArgumentException("This type cannot be saved into Preferences")
        }.apply()
    }

    private inline fun <reified T> observe(@StringRes prefKey: Int, default: T) = observe(name(prefKey), default)

    private inline fun <reified T> observe(name: String, default: T): LiveData<T> =
            object : SharedPreferenceLiveData<T>(preferences, name, default) {
                override fun getValueFromPreferences(key: String, default: T) =
                        get(key, default)
            }
}

private abstract class SharedPreferenceLiveData<T>(private val preferences: SharedPreferences,
                                                   private val key: String,
                                                   private val default: T) : LiveData<T>() {

    init {
        value = this.getValueFromPreferences(key, default)
    }

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) {
            value = getValueFromPreferences(key, default)
        }
    }

    abstract fun getValueFromPreferences(key: String, default: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, default)
        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}
