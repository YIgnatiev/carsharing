package youdrive.today;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
