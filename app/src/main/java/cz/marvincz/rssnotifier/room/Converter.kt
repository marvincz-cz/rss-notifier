package cz.marvincz.rssnotifier.room

import android.net.Uri

import androidx.room.TypeConverter

object Converter {
    @TypeConverter
    fun uriToString(uri: Uri) = uri.toString()

    @TypeConverter
    fun stringToUri(string: String) = Uri.parse(string)!!
}
