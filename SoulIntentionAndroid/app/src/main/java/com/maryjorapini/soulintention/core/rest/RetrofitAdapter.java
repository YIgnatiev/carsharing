package com.maryjorapini.soulintention.core.rest;


import com.maryjorapini.soulintention.core.APIConstants;
import com.squareup.okhttp.OkHttpClient;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Sasha on 20.11.2014.
 */
public abstract class RetrofitAdapter {
    private static String cookies;
    private  static  CustomCookieManager cookieManager;
    private static final String SESSION_KEY = "SessionName";
    public static String getCookies() {
        return cookies;
    }

    public static void setCookies(String cookies) {
        RetrofitAdapter.cookies = cookies;
    }

    /**
     * Injects cookies to every request
     */
    private static final RequestInterceptor COOKIES_REQUEST_INTERCEPTOR = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            if (null != cookies && cookies.length() > 0) {
                request.addHeader("Set-Cookie", SESSION_KEY + "=" + cookies);
            }
        }
    };
    private static RetrofitInterface retrofitInterface;
    private static RestAdapter restAdapter;
    public static RetrofitInterface getInterface() {

        if (retrofitInterface == null) {
            OkHttpClient client = new OkHttpClient();
            cookieManager = new CustomCookieManager();
            client.setCookieHandler(cookieManager);
            restAdapter = new RestAdapter.Builder().setEndpoint(APIConstants.SERVER_URL).setRequestInterceptor(COOKIES_REQUEST_INTERCEPTOR).setClient(new OkClient(client)).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.BASIC);
            retrofitInterface = restAdapter.create(RetrofitInterface.class);
        }
        return retrofitInterface;
    }

}
