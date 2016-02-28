package com.maryjorapini.soulintention.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.maryjorapini.soulintention.MainFragmentActivity;
import com.maryjorapini.soulintention.global.Constants;

/**
 * Created by Sasha on 17.10.2014.
 */
public abstract class SharedPreferencesManager {
    private static final String TAG = "GCMDemoShared";
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param _context application's context.
     * @param _regId  registration ID
     */
    public static void storeRegistrationId(final Context _context, final String _regId) {
        final SharedPreferences prefs = _context.getSharedPreferences(
                MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(_context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PARAM_REG_ID, _regId);
        editor.putInt(Constants.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Stores the mobile ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param _context application's context.
     * @param _deviceId  registration ID
     */

    public static void storeMobileId(final Context _context, final String _deviceId) {
        final SharedPreferences prefs = _context.getSharedPreferences(
                MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(_context);
        Log.i(TAG, "Saving mobileId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PARAM_DEVICE_ID, _deviceId);
        editor.putInt(Constants.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
