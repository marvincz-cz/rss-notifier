package cz.marvincz.rssnotifier.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import androidx.annotation.StringRes;
import cz.marvincz.rssnotifier.R;
import cz.marvincz.rssnotifier.RssApplication;

public final class PreferenceUtil {

    private PreferenceUtil() {
    }

    public static void initPreferences() {
        // TODO: 10.11.2018
    }

    private static Context getContext() {
        return RssApplication.getInstance();
    }

    public static Duration sinceLastDownload() {
        final Context context = getContext();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String preferencesString = preferences.getString(context.getString(R.string.preference_last_download),
                defaultTime());
        ZonedDateTime lastDownload = ZonedDateTime.parse(preferencesString);
        return Duration.between(lastDownload, ZonedDateTime.now());
    }

    public static void updateLastDownload() {
        Context context = getContext();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.preference_last_download), ZonedDateTime.now().toString())
                .apply();
    }

    private static String defaultTime() {
        return ZonedDateTime.now().minus(1, ChronoUnit.YEARS).toString();
    }

    private static String initPreference(@StringRes int preferenceKey, String defaultValue) {
        final Context context = getContext();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String key = context.getString(preferenceKey);
        if (preferences.contains(key)) {
            return preferences.getString(key, defaultValue);
        } else {
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, defaultValue);
            editor.apply();
            return defaultValue;
        }
    }
}
