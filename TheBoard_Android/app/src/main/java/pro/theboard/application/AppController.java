package pro.theboard.application;

import android.app.Application;

import com.bugsnag.android.Bugsnag;

import pro.theboard.utils.Preferences;


/**
 * Created by Admin on 21.02.2015.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(getApplicationContext());
        Bugsnag.init(this);
    }
}








