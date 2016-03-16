package ru.lead2phone.ru.lead2phone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleh Makhobey on 04.06.2015.
 * tajcig@ya.ru
 */
public class Preferences {


    private final static String USER_DATA = "user_data";
    private final static String IS_LOGGED = "is_logged";

    public final static String LOGIN = "login";
    public final static String DELETED = "deleted";
    public final static String PASSWORD = "password";
    public final static String USER_NAME = "user_name";


        private static SharedPreferences mSharedPreferences;

        public static void init(Context _context){
            mSharedPreferences = _context.getSharedPreferences(USER_DATA,Context.MODE_PRIVATE);
        }



        public static void saveIsLogged( boolean isLogged) {
            saveBoolean(IS_LOGGED,isLogged);
        }

        public static boolean isLogged() {
            return getBoolean(IS_LOGGED);
        }

        public static String getLogin(){
            return getString(LOGIN);
        }

        public static void setLogin(String login){
            saveString(LOGIN,login);
        }

        public static String getUserName(){
            return getString(USER_NAME);
        }

        public static void setUserName(String name){
            saveString(USER_NAME,name);
        }


     public static String getPassword(){
            return getString(PASSWORD);
        }

        public static void setPassword(String login){
            saveString(PASSWORD,login);
        }

        public static Set <String>getDeletedId(){
           return mSharedPreferences.getStringSet(DELETED,null);
        }

        public static void saveDeletedIds(List<String> list){
            Set <String>  stringSet = new HashSet<>();
            for(String id : list){
                stringSet.add(id);
            }

            mSharedPreferences.edit().putStringSet(DELETED,stringSet).apply();
        }



        private static void saveBoolean(String key,boolean value){
            mSharedPreferences.
                    edit()
                    .putBoolean(key,value)
                    .apply();
        }

        private static boolean getBoolean(String key){
            return mSharedPreferences.getBoolean(key,false);
        }

        private static void saveString(String _key, String _value){
            mSharedPreferences
                    .edit()
                    .putString(_key, _value)
                    .apply();
        }

        private static String getString(String _key){
            return mSharedPreferences.getString(_key, "");
        }




}
