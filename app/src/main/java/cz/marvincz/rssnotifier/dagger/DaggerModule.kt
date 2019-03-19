package cz.marvincz.rssnotifier.dagger

import javax.inject.Singleton

import cz.marvincz.rssnotifier.RssApplication
import cz.marvincz.rssnotifier.repository.Repository
import cz.marvincz.rssnotifier.room.Database
import dagger.Module
import dagger.Provides

@Module
class DaggerModule {
    @Provides
    @Singleton
    internal fun provideDatabase(): Database {
        return Database.get(RssApplication.instance)
    }

    @Provides
    @Singleton
    internal fun provideRepository(): Repository {
        return Repository()
    }
}
