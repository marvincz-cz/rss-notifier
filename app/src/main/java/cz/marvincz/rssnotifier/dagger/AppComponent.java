package cz.marvincz.rssnotifier.dagger;

import javax.inject.Singleton;

import cz.marvincz.rssnotifier.activity.MainActivity;
import cz.marvincz.rssnotifier.repository.Repository;
import dagger.Component;

@Singleton
@Component(modules = DaggerModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(Repository repository);
}
