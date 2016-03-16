package ru.lead2phone.ru.lead2phone;

import android.app.Application;

import ru.lead2phone.ru.lead2phone.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/27/15.
 */
public class Lead2Phone extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(getApplicationContext());
    }
}
