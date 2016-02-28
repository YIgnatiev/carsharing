package com.carusselgroup.dwt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class TSharedPrefferenses {
    private static String PREFF_NAME = "com.carusselgroup.dwt.utils";
    private static String FIELD_NAME = "FIELD_NAME";
    private static String FIELD_PASSWORD = "FIELD_PASSWORD";

    public static void saveUser(Context context, String userName, String userPassword){
        SharedPreferences prefs = context.getSharedPreferences(
                PREFF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIELD_NAME, userName);
        editor.putString(FIELD_PASSWORD, userPassword);
        editor.apply();
    }

    public static List<String> getUserData(Context context){
        List<String> userData = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(
                PREFF_NAME, Context.MODE_PRIVATE);
        if (prefs.getString(FIELD_NAME, "").equals(""))
            return userData;
        userData.add(prefs.getString(FIELD_NAME, ""));
        userData.add(prefs.getString(FIELD_PASSWORD, ""));
        return userData;
    }

    public static void logout(Context context){
        saveUser(context, "", "");
    }
}
