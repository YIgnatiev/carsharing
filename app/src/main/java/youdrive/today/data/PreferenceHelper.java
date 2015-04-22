package youdrive.today.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by psuhoterin on 22.04.15.
 */
public class PreferenceHelper {

    protected static final String USER_FILE = "user.xml";
    private static final String PREF_COOKIES = "youdrive.today.data.PREF_COOKIES";

    private Context mContext;

    public PreferenceHelper(Context context) {
        this.mContext = context;
    }

    public void putStringSet(HashSet<String> cookies){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREF_COOKIES, 0).edit();
        editor.putStringSet(PREF_COOKIES, cookies).apply();
    }

    public Set<String> getStringSet(){
        return mContext.getSharedPreferences(PREF_COOKIES, 0).getStringSet(PREF_COOKIES, null);
    }
}
