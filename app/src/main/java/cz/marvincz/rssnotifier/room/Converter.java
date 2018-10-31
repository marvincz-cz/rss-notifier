package cz.marvincz.rssnotifier.room;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converter {
    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri.toString();
    }

    @TypeConverter
    public static Uri stringToUri(String string) {
        return Uri.parse(string);
    }
}
