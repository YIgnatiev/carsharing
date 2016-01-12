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
    private static Tracker mTracker;
    private static GoogleAnalytics analytics;

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
        analytics = GoogleAnalytics.getInstance(this);
        mTracker = analytics.newTracker("UA-64074244-1");
        mTracker.enableExceptionReporting(true);
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.enableAutoActivityTracking(true);

        YandexMetrica.enableActivityAutoTracking(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public PreferenceHelper getPreference(){
        return mPreference;
    }
    public static Tracker tracker() {
        return mTracker;
    }

    public static GoogleAnalytics analytics() {
        return analytics;
    }




    public ApiClient getApiClient(){
        return mApiClient;
    }

    public static App getInstance(){
        return sInstance;
    }

}
