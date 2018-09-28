package marvincz.cz.rssnotifier.application;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class RssApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
