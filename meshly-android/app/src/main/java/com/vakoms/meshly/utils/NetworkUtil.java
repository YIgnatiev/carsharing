package com.vakoms.meshly.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vakoms.meshly.BaseActivity;

/**
 * Created by taras.melko on 8/18/14.
 */
public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        if (context == null)
            return true;
        if (context.getSystemService(Context.CONNECTIVITY_SERVICE) == null)
            return true;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            ConnectionDialog dialog = new ConnectionDialog();
            dialog.show(((BaseActivity) context).getFragmentManager(), "INTERNET");
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
