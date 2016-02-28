package com.morfitrun.api.services;

import com.morfitrun.data_models.NewRunner;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Admin on 20.03.2015.
 */
public interface AddRunnerService {
    @POST("/runner")
    public void addRunner(@Body NewRunner _runner, Callback<NewRunner> _callback);
}
