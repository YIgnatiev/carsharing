package com.maryjorapini.soulintention.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.maryjorapini.soulintention.MainFragmentActivity;
import com.maryjorapini.soulintention.global.Constants;

import java.util.UUID;

/**
 * Created by Sasha on 17.10.2014.
 */
public abstract class DeviceManager {
    private static final String TAG = "DeviceManager";

    private static String loadDeviceId(final Context _context) {
        final SharedPreferences prefs = _context.getSharedPreferences(MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String mobileId = prefs.getString(Constants.PARAM_DEVICE_ID, "");
        if (mobileId.isEmpty()) {
            Log.i(TAG, "Device id not found.");
            mobileId = generateDeviceUuid(_context);
            SharedPreferencesManager.storeMobileId(_context, mobileId);
        }
        int registeredVersion   = prefs.getInt(Constants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion      = SharedPreferencesManager.getAppVersion(_context);
        if (registeredVersion  != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return mobileId;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */

    public static String loadRegistrationId(final Context _context) {
        final SharedPreferences prefs = _context.getSharedPreferences(MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(Constants.PARAM_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";

        }
        int registeredVersion   = prefs.getInt(Constants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion      = SharedPreferencesManager.getAppVersion(_context);
        if (registeredVersion  != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static String generateDeviceUuid(Context _context) {
        String deviceUuid;

        final TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice    = "" + tm.getDeviceId();
        tmSerial    = "" + tm.getSimSerialNumber();
        androidId   = "" + android.provider.Settings.Secure.getString(_context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID uuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceUuid = uuid.toString();
        return deviceUuid;
    }

}
