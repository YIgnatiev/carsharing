package com.morfitrun.api.services;

import com.morfitrun.data_models.RunnerUser;
import com.morfitrun.data_models.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Admin on 24.03.2015.
 */
public interface GetUserService {
    @GET("/user/{id}")
    public void getUser(@Path("id")String userId, Callback<RunnerUser> _callback);
}
