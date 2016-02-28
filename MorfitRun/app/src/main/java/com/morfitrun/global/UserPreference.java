package com.morfitrun.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vasia on 26.03.2015.
 */
public class UserPreference {

    public static final void clearPreference(Context _context){
        writeUserToPreferences(_context, "", "");
    }

    public static final void writeUserToPreferences(final Context _context, final String _userLogin, final String _userPassword){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_LOGIN, _userLogin);
            editor.putString(Constants.USER_PASSWORD, _userPassword);
        editor.apply();
    }

    public static final String getCurrentUserLogin(final Context _context){
        SharedPreferences preferences = _context.getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        final String userLogin = preferences.getString(Constants.USER_LOGIN, "");
        return userLogin;
    }

    public static final String getCurrentUserPassword(final Context _context){
        SharedPreferences preferences = _context.getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
        final String userPassword = preferences.getString(Constants.USER_PASSWORD, "");
        return userPassword;
    }
}
