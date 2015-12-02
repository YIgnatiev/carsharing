package youdrive.today;

import android.app.Application;

import timber.log.Timber;
import youdrive.today.helpers.PreferenceHelper;
import youdrive.today.network.ApiClient;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class App extends Application {

    private static App sInstance;
    private PreferenceHelper mPreference;
    private ApiClient mApiClient;

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPreference = new PreferenceHelper(getApplicationContext());
        mApiClient = new ApiClient();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public PreferenceHelper getPreference(){
        return mPreference;
    }

    public ApiClient getApiClient(){
        return mApiClient;
    }

    public static App getInstance(){
        return sInstance;
    }

}
