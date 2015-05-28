package youdrive.today.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import youdrive.today.R;

public class PreferenceHelper {

    protected static final String USER_FILE = "youdrive.today.data.USER_FILE";
    protected static final String SESSION_ID = "youdrive.today.data.SESSION_ID";
    private final SharedPreferences mHelper;

    public PreferenceHelper(Context context) {
        mHelper = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(){
        return mHelper.edit();
    }

    public void putSession(HashSet<String> cookies){
        getEditor().putStringSet(SESSION_ID, cookies).apply();
    }

    public Set<String> getSession(){
        return mHelper.getStringSet(SESSION_ID, null);
    }

    public void putUser(String user){
        getEditor().putString(USER_FILE, user).apply();
    }

    public String getUser(){
        return mHelper.getString(USER_FILE, null);
    }

    public void clear(){
        getEditor().clear().apply();
    }
}
