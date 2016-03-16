package ru.lead2phone.ru.lead2phone.rest;

import retrofit.RequestInterceptor;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class RequestHeaders implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {

        request.addHeader("Authorization",
                String.format("%s %s", "", ""));
    }
}
