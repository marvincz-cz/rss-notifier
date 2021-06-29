package cz.marvincz.rssnotifier.room

import android.net.Uri

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converter {
    @TypeConverter
    fun uriToString(uri: Uri) = uri.toString()

    @TypeConverter
    fun stringToUri(string: String) = Uri.parse(string)!!

    @TypeConverter
    fun dateToLong(dateTime: ZonedDateTime): Long = dateTime.toInstant().toEpochMilli()

    @TypeConverter
    fun longToDate(long: Long): ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(long), ZoneId.systemDefault())
}
