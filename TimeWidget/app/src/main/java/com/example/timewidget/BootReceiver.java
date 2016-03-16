package com.example.timewidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
          // Intent serivceIntent = new Intent(context, TimeService.class);



        }
    }
//    private Intent readPreferences(Intent intent){
//
//        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
//        intent.putExtra(VERTICAL_POSITION, preferences.getInt(VERTICAL_POSITION, 0));
//        intent.putExtra(HORIZONTAL_POSITION, preferences.getInt(HORIZONTAL_POSITION, 0));
//        return intent;
//    }

}
