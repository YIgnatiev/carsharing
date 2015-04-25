package youdrive.today;

import android.app.Application;

import com.google.gson.Gson;

import timber.log.Timber;
import youdrive.today.data.PreferenceHelper;
import youdrive.today.data.network.ApiClient;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class App extends Application {

    private static App sInstance;
    private PreferenceHelper mPreference;
    private ApiClient mApiClient;
    private Gson mGson;

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPreference = new PreferenceHelper(getApplicationContext());
        mApiClient = new ApiClient();
        mGson = new Gson();

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

    public Gson getGson(){
        return mGson;
    }

    public static App getInstance(){
        return sInstance;
    }
}
