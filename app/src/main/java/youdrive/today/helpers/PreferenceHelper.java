package youdrive.today.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.Set;

import youdrive.today.R;

public class PreferenceHelper {

    protected static final String USER_FILE = "youdrive.today.data.USER_FILE";
    protected static final String SESSION_ID = "youdrive.today.data.SESSION_ID";
    protected static final String INVITE = "youdrive.today.data.INVITE";
    protected static final String IS_FIRST = "youdrive.today.data.ISFIRST";

    /// Координаты центра Москвы
    public static final LatLng MOSCOW_CENTER = new LatLng(55.749792, 37.632495);
    public static final String EULA_URL = "https://youdrive.today/static/text/eula/eula.html";
    //public static final String EULA_URL="https://youdrive.today/static/text/eula/eula.txt";

    private final SharedPreferences mHelper;

    public PreferenceHelper(Context context) {
        mHelper = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mHelper.edit();
    }

    public void putSession(Set<String> cookies) {
        getEditor().putStringSet(SESSION_ID, cookies).apply();
    }

    public Set<String> getSession() {
        return mHelper.getStringSet(SESSION_ID, null);
    }

    public void putUser(String user) {
        getEditor().putString(USER_FILE, user).apply();
    }

    public void logout() {
        getEditor().clear().apply();
    }

    public String getUser() {
        return mHelper.getString(USER_FILE, null);
    }

    public void putInvite(boolean b) {
        getEditor().putBoolean(INVITE, b).apply();
    }

    public boolean isInvite() {
        return mHelper.getBoolean(INVITE, true);
    }


    public boolean isFirst() {
        return mHelper.getBoolean(IS_FIRST, true);
    }

    public void setIsFirst() {
        mHelper.edit().putBoolean(IS_FIRST, false).commit();
    }

    public void clear() {
        getEditor().clear().apply();
    }
}
