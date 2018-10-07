package cz.marvincz.rssnotifier;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import cz.marvincz.rssnotifier.dagger.AppComponent;
import cz.marvincz.rssnotifier.dagger.DaggerAppComponent;

public class RssApplication extends Application {
    private static RssApplication instance;
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = DaggerAppComponent.builder().build();

        AndroidThreeTen.init(this);
    }

    public static RssApplication getInstance() {
        return instance;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
