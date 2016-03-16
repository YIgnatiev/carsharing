package com.vakoms.meshly.utils;

/**
 * Created by Sviatoslav Kashchin on 08.01.15.
 */
public class TokenValidator {
    private static final String TAG = "TokenValidator";

    private static final Object locker = new Object();

    /**
     * If token expires in less then 60 sec (60000 ms) then you should refresh it
     *
     * @return true if token expires in less then 60 sec
     */
    public static boolean accessTokenExpiresSoon() {
        synchronized (locker) {
            long timeLeft = P.getTokenExpireTime() - System.currentTimeMillis();
            boolean expiresSoon = timeLeft < 60000;

            if (expiresSoon) {
                Logger.w(TAG, "access token expires soon (or expired), need to be refreshed! [time left = " + timeLeft + "]");
            }

            return expiresSoon;
        }
    }

    /**
     * Check if app has access tokens, left from previous login
     *
     * @return true if both access and refresh token available.
     * Otherwise false.
     */
    public static boolean hasAccessTokens() {
        return (!P.getAccessToken().equals("") && !P.getRefreshToken().equals(""));
    }
}