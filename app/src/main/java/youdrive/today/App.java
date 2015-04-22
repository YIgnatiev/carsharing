package youdrive.today;

import android.app.Application;

import timber.log.Timber;
import youdrive.today.data.PreferenceHelper;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class App extends Application {

    private static App sInstance;
    private PreferenceHelper mPreference;

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPreference = new PreferenceHelper(getApplicationContext());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public PreferenceHelper getPreference(){
        return mPreference;
    }

    public static App getInstance(){
        return sInstance;
    }
}
