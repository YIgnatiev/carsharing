package com.morfitrun.api.services;

import com.morfitrun.data_models.Runner;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Admin on 19.03.2015.
 */
public interface GetRunnersService {
    @GET("/runner/{raceId}")
    public void getRunners(@Path("raceId") String _raceId, Callback<List<Runner>> _callback);

}
