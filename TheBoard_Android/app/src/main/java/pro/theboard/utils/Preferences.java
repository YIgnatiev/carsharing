package pro.theboard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Oleh Makhobey on 04.06.2015.
 * tajcig@ya.ru
 */
public class Preferences {

        private final static String USER_DATA = "user_data";

        private final static String LOCATION = "location";
        private final static String NOTIFICATION = "notification";
        private final static String CUSTOMER_HASH = "customer_hash";
        private final static String IS_ADULT = "is_aduld";
        private final static String VIDEO_LOADED = "videoLoaded";
        private final static String ANSWERED_QUESTIONS = "answered_questions";

        private static SharedPreferences mSharedPreferences;

        public static void init(Context _context){
            mSharedPreferences = _context.getSharedPreferences(USER_DATA,Context.MODE_PRIVATE);
        }



        public static void saveCustomerHash( String _token) {
            saveString(CUSTOMER_HASH,_token);
        }

        public static String getCustomerHash() {
            return getString(CUSTOMER_HASH);
        }


        public static void saveLocationPreference(boolean _value){
            saveBoolean(LOCATION,_value);
        }

        public static boolean getLocationPreference(){
            return getBoolean(LOCATION);
        }

        public static void saveNotificationPreference( boolean value){
            saveBoolean(NOTIFICATION,value);
        }

        public static boolean getNotificationPreference(){
            return getBoolean(NOTIFICATION);
        }


        public static void saveIsAdult(boolean _isAdult){
            saveBoolean(IS_ADULT,_isAdult);
        }

        public static boolean getIsAdult(){
            return mSharedPreferences.getBoolean(IS_ADULT,false);
        }



//        public static Set<String> getAnsweredQuestions(){
//            return mSharedPreferences.getStringSet(ANSWERED_QUESTIONS, null);
//        }
//
//        public static void addAnsweredQuestion(String answerHash){
//
//            Set<String> answerList = getAnsweredQuestions();
//            if(answerList == null){
//                answerList = new HashSet<>();
//            }else{
//                answerList = new HashSet<>(answerList);
//            }
//            answerList.add(answerHash);
//
//            mSharedPreferences.edit().putStringSet(ANSWERED_QUESTIONS,answerList).apply();
//        }

        private static void saveBoolean(String key,boolean value){
            mSharedPreferences.
                    edit()
                    .putBoolean(key,value)
                    .apply();
        }

        private static boolean getBoolean(String key){
            return mSharedPreferences.getBoolean(key,true);
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


    public static void setVideoLoaded(boolean b) {
        saveBoolean(VIDEO_LOADED,b);
    }
}
