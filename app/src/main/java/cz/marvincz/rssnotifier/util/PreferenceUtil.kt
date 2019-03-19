package cz.marvincz.rssnotifier.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.StringRes
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.RssApplication
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

object PreferenceUtil {
    private val context: Context by lazy { RssApplication.instance }

    fun initPreferences() {
        // TODO: 10.11.2018
    }

    fun sinceLastDownload(): Duration {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val preferencesString = preferences.getString(context.getString(R.string.preference_last_download),
                defaultStartTime())
        val lastDownload = ZonedDateTime.parse(preferencesString!!)
        return Duration.between(lastDownload, ZonedDateTime.now())
    }

    fun updateLastDownload() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.preference_last_download), ZonedDateTime.now().toString())
                .apply()
    }

    private fun defaultStartTime() = ZonedDateTime.now().minus(1, ChronoUnit.YEARS).toString()

    private fun initPreference(@StringRes preferenceKey: Int, defaultValue: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = context.getString(preferenceKey)
        if (preferences.contains(key)) {
            return preferences.getString(key, defaultValue)
        } else {
            val editor = preferences.edit()
            editor.putString(key, defaultValue)
            editor.apply()
            return defaultValue
        }
    }
}
