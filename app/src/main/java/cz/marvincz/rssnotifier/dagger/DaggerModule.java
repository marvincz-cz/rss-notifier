package cz.marvincz.rssnotifier.dagger;

import javax.inject.Singleton;

import cz.marvincz.rssnotifier.RssApplication;
import cz.marvincz.rssnotifier.room.Database;
import dagger.Module;
import dagger.Provides;

@Module
public class DaggerModule {
    @Provides
    @Singleton
    Database provideDatabase() {
        return Database.get(RssApplication.getInstance());
    }
}
