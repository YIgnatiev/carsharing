package com.morfitrun.api.services;

import com.morfitrun.data_models.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Виталий on 16/03/2015.
 */
public interface SignInService {

    @POST("/signIn")
    public void signIn(@Body User _user, Callback<User> _callback);

}
