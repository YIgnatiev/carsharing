package com.vakoms.meshly.utils;
/**
 *
 * @author andrew.volosko
 * @modified & updated by Sviatoslav Kashchin
 * @desciption Easy way to remove logs from production build
 */

import android.util.Log;

import meshly.vakoms.com.meshly.BuildConfig;

public class Logger {

    public static final boolean SHOW_METHOD_NAME = true;

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, getMethodName() + message);
        }
    }

    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, getMethodName() + message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, getMethodName() + message);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, getMethodName() + message);
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, getMethodName() + message);
        }
    }

    public static void wtf(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.wtf(tag, getMethodName() + message);
        }
    }

    private static String getMethodName() {
        if (SHOW_METHOD_NAME) {
            /**
             * 0 = would return currentThread
             * 1 = getStackTrace
             * 2 = current method (getMethodName)
             * 3 = log method (i, w, d etc)
             * 4 - log caller name
             */
            return Thread.currentThread().getStackTrace()[4].getMethodName() + " $ ";
        }

        return "";
    }
}