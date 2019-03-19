package cz.marvincz.rssnotifier

import android.app.Application

import com.jakewharton.threetenabp.AndroidThreeTen

import cz.marvincz.rssnotifier.dagger.AppComponent
import cz.marvincz.rssnotifier.dagger.DaggerAppComponent

class RssApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        instance = this
        appComponent = DaggerAppComponent.builder().build()
    }

    companion object {
        lateinit var instance: RssApplication
            private set
        lateinit var appComponent: AppComponent
            private set
    }
}
