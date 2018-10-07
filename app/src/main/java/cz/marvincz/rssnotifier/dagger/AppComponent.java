package cz.marvincz.rssnotifier.dagger;

import javax.inject.Singleton;

import cz.marvincz.rssnotifier.activity.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = DaggerModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);
}
