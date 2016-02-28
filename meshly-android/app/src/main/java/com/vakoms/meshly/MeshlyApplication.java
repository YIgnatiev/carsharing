package com.vakoms.meshly;

import android.app.Activity;
import android.app.Application;

import com.bugsnag.android.Bugsnag;
import com.bugsnag.android.MetaData;
import com.bugsnag.android.Severity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.vakoms.meshly.utils.P;

//import meshly.vakoms.com.meshly.BuildConfig;

/**
 * Created by taras.melko on 9/4/14.
 */
public class MeshlyApplication extends Application {

    public static final String TAG = MeshlyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();


        P.initSharedPrefferences(getSharedPreferences(P.PREF_NAME, Activity.MODE_PRIVATE));
        Bugsnag.init(getApplicationContext(), "c5e3203918e20c8908f2089ddb9f13c5");

        Fresco.initialize(getApplicationContext());
    }



    public void notifyError(Severity severity, String description) {
        MetaData metaData = new MetaData();
        metaData.addToTab("error_details", "description", description);


        Bugsnag.notify(new Throwable("dummy"), severity, metaData);
    }
}