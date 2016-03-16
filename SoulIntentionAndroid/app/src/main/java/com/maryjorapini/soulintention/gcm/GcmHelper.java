package com.maryjorapini.soulintention.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maryjorapini.soulintention.global.Constants;

import java.io.IOException;

/**
 * Created by Sasha on 17.10.2014.
 */
public class GcmHelper {
    private GoogleCloudMessaging mGcm;
    private Context mContext;
    private Activity mActivity;
    private String regId;
    private String TAG = "GcmHelper";

    public GcmHelper(final Activity _activity) {
        this.mActivity = _activity;
        this.mContext = _activity.getApplicationContext();
    }

    public void registerDevice() {
        if (checkPlayServices()) {
            mGcm    = GoogleCloudMessaging.getInstance(mContext);
            regId   = DeviceManager.loadRegistrationId(mContext);

            if (regId.isEmpty()) {
                registerInBackground();
            }
        } else{
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        if (checkPlayServices()) {
            Log.i(TAG, "Play Service Exist");
            mGcm    = GoogleCloudMessaging.getInstance(mContext);
            regId   = DeviceManager.loadRegistrationId(mContext);
            if (regId.isEmpty()) {
                registerInBackground();
            } else {
                Log.i(TAG, "From Shared Pref " + regId);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        int resultCode  = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                mActivity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */

    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            protected void onPostExecute(String msg) {
                Log.d(TAG, msg);
//                Variables.setPush_id(regId);
                SharedPreferencesManager.storeRegistrationId(mContext, regId);
            }
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regId = mGcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

        }.execute(null, null, null);

    }

}
