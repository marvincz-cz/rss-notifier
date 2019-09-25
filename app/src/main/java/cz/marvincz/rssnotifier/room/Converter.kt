package cz.marvincz.rssnotifier.room

import android.net.Uri

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

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
