package com.maryjorapini.soulintention.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.maryjorapini.soulintention.MainFragmentActivity;

import java.util.UUID;

/**
 * Created by Sasha on 17.10.2014.
 */
public abstract class DeviceManager {
    private static final String TAG = "Device_id";
    /**
     * Stores the mobile ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param _context application's context.
     * @param _deviceId  registration ID
     */

    private static void storeMobileId(final Context _context, final String _deviceId) {
        final SharedPreferences prefs = _context.getSharedPreferences(
                MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(_context);
        Log.i("Device_id", "Saving mobileId on app version " + appVersion);
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

    public static String loadDeviceId(final Context _context) {
        final SharedPreferences prefs = _context.getSharedPreferences(MainFragmentActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String mobileId = prefs.getString(Constants.PARAM_DEVICE_ID, "");
        if (mobileId.isEmpty()) {
            Log.i(TAG, "Device id not found.");
            mobileId = generateDeviceUuid(_context);
            DeviceManager.storeMobileId(_context, mobileId);
        }
        int registeredVersion   = prefs.getInt(Constants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion      = DeviceManager.getAppVersion(_context);
        if (registeredVersion  != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return mobileId;
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
