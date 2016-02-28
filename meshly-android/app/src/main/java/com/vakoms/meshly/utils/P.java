package com.vakoms.meshly.utils;

import android.content.SharedPreferences;
import android.location.Location;

import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.models.Geo;
import com.vakoms.meshly.models.Privacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P {

    public static final String USER_HIDDEN_STATE_PREFS_KEY = "user_hide_from_everyone_hr";
    public static final String USER_TIMESTAMP_PREFS_KEY = "user_timestamp_state";
    public static final String USER_LOCATION_PREFS_KEY = "user_location_state";
    public static final String USER_MESSAGING_PREFS_KEY = "user_messaging_state";

    public static final String PREF_NAME = "meshly";


    private static final String IS_FIRST_TIME = "is_first_time";
    private static final String IS_LOGGED_WITH_ANOTHER_DEVICE = "is_logged_with_another_device";


    private static final String USER_NAME = "name";

    private static final String NEW_MESSAGE = "new";

    private static final String ADD_EVENTS_ADDRESS = "adress_e";
    private static final String ADD_EVENTS_DESCRIPTION = "description_e";
    private static final String ADD_EVENTS_SKILLS = "skills_e";
    private static final String SECURITY_ACCESS_TOKEN = "access_token";
    private static final String SECURITY_REFRESH_TOKEN = "refresh_token";
    private static final String SECURITY_TOKEN_EXPIRE_TIME = "access_token_expire_time";
    private static final String SECURITY_TOKEN_TYPE = "token_type";
    private static final String TAG = "Preference";

    private static final String EVENTS_BEGIN = "BEGIN";
    private static final String EVENTS_END = "END";
    private static final String EVENTS_BEGIN_DATE = "begin_data";
    private static final String EVENTS_END_DATE = "end_data";

    private static SharedPreferences sharedPreferences = null;


    public static void initSharedPrefferences(SharedPreferences _sharedPreferences){
        sharedPreferences = _sharedPreferences;
    }

    public static SharedPreferences getSharedPreferences() {

        return sharedPreferences;
    }


    public static boolean isFirstTime(){
        return getBoolean(IS_FIRST_TIME,true);
    }

    public static void saveIsFirstTime(boolean value){
        saveBoolean(value,IS_FIRST_TIME);
    }


    public static boolean isLoggedWithAnotherDevice(){
        return getBoolean(IS_LOGGED_WITH_ANOTHER_DEVICE,false);
    }

    public static void saveIsLoggedWithAnotherDevice(boolean value){
        saveBoolean(value, IS_LOGGED_WITH_ANOTHER_DEVICE);
    }












    public static void saveNewMessage(String userId) {
        saveString(NEW_MESSAGE, userId);
    }

    public static String getNewMessage() {
        return getString(NEW_MESSAGE);
    }






    public static String getUsername() {
        return getString(USER_NAME);
    }

















    public static void saveAddEventsAdress(String data) {
        saveString(ADD_EVENTS_ADDRESS, data);
    }

    public static String getAddEventsAddress() {
        return getString(ADD_EVENTS_ADDRESS);
    }

    public static void saveAddEventsDescription(String data) {
        saveString(ADD_EVENTS_DESCRIPTION, data);
    }

    public static String getAddEventsDescription() {
        return getString(ADD_EVENTS_DESCRIPTION);
    }

    public static void saveAddEventsSkills(String data) {
        saveString(ADD_EVENTS_SKILLS, data);
    }

    public static String getAddEventsSkills() {
        return getString(ADD_EVENTS_SKILLS);
    }

    public static void saveAddEventsBegin(String data) {
        saveString(EVENTS_BEGIN, data);
    }



    public static void saveAddEventsEnd(String data) {
        saveString(EVENTS_END, data);
    }



    public static void saveBegin(long data) {
        saveLong(EVENTS_BEGIN_DATE, data);
    }


    public static void saveEnd(long data) {
        saveLong(EVENTS_END_DATE, data);
    }

    public static long getEnd() {
        return getLong(EVENTS_END_DATE);
    }

    public static void clearData() {
        getSharedPreferences()
                .edit()
                .clear()
                .apply();
    }



    public static void fillPrivacy(Privacy userPrivacy) {
        if (userPrivacy == null) {
            userPrivacy = new Privacy();
        }

        SharedPreferences preferences = P.getSharedPreferences();
        userPrivacy.setHidden(preferences.getLong(USER_HIDDEN_STATE_PREFS_KEY, System.currentTimeMillis()));
        userPrivacy.setLastSeen(preferences.getString(USER_TIMESTAMP_PREFS_KEY, Constants.privacyCasesForRequest[0]));
        userPrivacy.setLocation(preferences.getString(USER_LOCATION_PREFS_KEY, Constants.privacyCasesForRequest[0]));
        userPrivacy.setChat(preferences.getString(USER_MESSAGING_PREFS_KEY, Constants.privacyCasesForRequest[0]));
    }

    /**
     * Save user credentials.
     *
     * @param accessToken  access token. Used in header in all requests
     * @param refreshToken refresh token. Used to postpone expiration time of access token
     * @param expireTime   access token expire time
     * @param type         token type
     */
    public static void saveTokens(String accessToken, String refreshToken, String expireTime, String type) {
        if (accessToken.equals("") || refreshToken.equals("") || expireTime.equals("")) {
            Logger.e(TAG, "some token or expire time is empty, can't save");
            return;
        }

        SharedPreferences preferences = P.getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SECURITY_ACCESS_TOKEN, accessToken);
        editor.putString(SECURITY_REFRESH_TOKEN, refreshToken);
        editor.putLong(SECURITY_TOKEN_EXPIRE_TIME, System.currentTimeMillis() + (Long.parseLong(expireTime) * 1000));
        editor.putString(SECURITY_TOKEN_TYPE, type);

        editor.apply();
    }


    public static String getAccessToken() {
        return getString(SECURITY_ACCESS_TOKEN);
    }


    public static String getRefreshToken() {
        return getString(SECURITY_REFRESH_TOKEN);
    }


    public static long getTokenExpireTime() {
        return getLong(SECURITY_TOKEN_EXPIRE_TIME, -1);
    }


    public static String getTokenType() {
        String token = getString(SECURITY_TOKEN_TYPE);
        if(token.isEmpty())token = "Bearer";
        return token;
    }


    public static void removeTokens() {
        SharedPreferences preferences = P.getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SECURITY_ACCESS_TOKEN, "");
        editor.putString(SECURITY_REFRESH_TOKEN, "");
        editor.putLong(SECURITY_TOKEN_EXPIRE_TIME, -1);
        editor.putString(SECURITY_TOKEN_TYPE, "");

        editor.apply();
    }


















    private static String getString(String _key) {
        return getString(_key, "");
    }

    private static String getString(String _key, String _emptyValue) {
        return getSharedPreferences().getString(_key, _emptyValue);
    }

    private static void saveString(String _key, String _value) {
        if (_value == null) _value = "";
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(_key, _value);
        editor.apply();
    }


    private static long getLong(String _key) {
        return getLong(_key,0);
    }

    private static long getLong(String _key , long _emptyValue){
        return getSharedPreferences().getLong(_key, _emptyValue);
    }

    private static void saveLong(String _key, long _value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(_key, _value);
        editor.apply();
    }


    private static void saveList(String _key, List<String> _valueList) {
        Set<String> valueSet = new HashSet<>();
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        if (_valueList != null) {
            for (String skills : _valueList) {
                valueSet.add(skills);
            }
        }
        editor.putStringSet(_key, valueSet);
        editor.apply();
    }


    private static ArrayList<String> getList(String _key) {
        ArrayList<String> list = new ArrayList<>();
        try {
            Set<String> set = getSharedPreferences().getStringSet(_key, Collections.<String>emptySet());

            list.addAll(set);
        }catch(ClassCastException e){
            return new ArrayList<>();
        }
        return list;
    }


    private static boolean getBoolean(String _key,boolean defValue){
        return getSharedPreferences().getBoolean(_key,defValue);
    }

    private static void saveBoolean(boolean value, String key){
        getSharedPreferences().edit().putBoolean(key,value).apply();
    }


    public static class GPS {
        private final static int FRESH_TIME = 300000;

        private static String LAST_SAVE_TIME = "last_save_time";
        private static String LONGITUDE = "longitude";
        private static String LATITUDE = "latitude";

        public static void saveLocation(Location location) {
            SharedPreferences preferences = P.getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(LONGITUDE, String.valueOf(location.getLongitude()));
            editor.putString(LATITUDE, String.valueOf(location.getLatitude()));

            editor.putLong(LAST_SAVE_TIME, System.currentTimeMillis());

            editor.apply();
        }

        public static double getLatitude() {
            String latitude = getSharedPreferences().getString(LATITUDE, "");
            if(!latitude.isEmpty())
            return Double.parseDouble(latitude);
            else return 0;
        }

        public static double getLongitude() {
            String longitude = getSharedPreferences().getString(LONGITUDE, "");
            if(!longitude.isEmpty())
            return Double.parseDouble(longitude);
            else return 0;
        }

        /**
         * Get location. It returns "reverted" coords, lat = lon, lon = lat, because Meshly srv has bug.
         *
         * @return reverted coords
         */
        public static Location getLocation() {
            Location location = new Location("saved");
            location.setLatitude(getLatitude());
            location.setLongitude(getLongitude());

            return location;
        }

        public static Geo getGeo(){
            return new Geo(getLatitude(),getLongitude());
        }


        public static long lastSaveTime() {
            return getSharedPreferences().getLong(GPS.LAST_SAVE_TIME, 0);
        }

        /**
         * @return if last location time is less then {@value com.vakoms.meshly.utils.P.GPS#FRESH_TIME} ms
         */
        public static boolean hasFreshLocation() {
            return System.currentTimeMillis() - lastSaveTime() < FRESH_TIME;
        }
    }
}
