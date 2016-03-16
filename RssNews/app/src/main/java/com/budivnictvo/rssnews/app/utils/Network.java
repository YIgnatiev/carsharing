package com.budivnictvo.rssnews.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Администратор on 07.01.2015.
 */
public abstract class Network {
    /**
     * check existing internet connection
     * @param _context
     * @return
     */
    public static final boolean isInternetConnectionAvailable(final Context _context) {
        final ConnectivityManager connectivityManager   = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo             = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
