package youdrive.today;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yandex.metrica.YandexMetrica;

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
    private Tracker mTracker;
    private final String YANDEX_API_KEY = "14cbd311-8785-44c6-97e5-4622679719f5";
    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPreference = new PreferenceHelper(getApplicationContext());
        mApiClient = new ApiClient();
        YandexMetrica.activate(getApplicationContext(), YANDEX_API_KEY);
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public PreferenceHelper getPreference(){
        return mPreference;
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
          //  mTracker = analytics.newTracker(R.xml.global_tracker);
//
        }
        return mTracker;
    }

    public ApiClient getApiClient(){
        return mApiClient;
    }

    public static App getInstance(){
        return sInstance;
    }

}
