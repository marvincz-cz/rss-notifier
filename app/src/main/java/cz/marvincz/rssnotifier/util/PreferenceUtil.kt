package cz.marvincz.rssnotifier.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.squareup.moshi.Moshi
import cz.marvincz.rssnotifier.R
import org.koin.core.KoinComponent
import org.koin.core.inject

object PreferenceUtil : KoinComponent {
    private val context : Context by inject()
    private val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    private val moshi : Moshi = Moshi.Builder()
            .build()

    fun getChannelOrder(): List<String> {
        val default = emptyList<String>().toJson()
        val json = preferences.getString(context.getString(R.string.preference_channel_order), default) ?: default
        return json.fromJson<List<String>>() ?: emptyList()
    }

    fun setChannelOrder(channelOrder: List<String>) {
        val json = channelOrder.toJson()
        preferences.edit().putString(context.getString(R.string.preference_channel_order), json).apply()
    }

    private inline fun <reified T> T.toJson()
        = moshi.adapter<T>(T::class.java).toJson(this)

    private inline fun <reified T> String.fromJson()
        = moshi.adapter<T>(T::class.java).fromJson(this)
}