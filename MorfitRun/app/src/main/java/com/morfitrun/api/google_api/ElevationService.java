package com.morfitrun.api.google_api;

import com.morfitrun.data_models.elevation_model.ElevationCallback;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by vasia on 23.03.2015.
 */
public interface ElevationService {

    @GET("/elevation/json")
    public void getElevation(@Query("locations")String location, @Query("sensor") String sensor, Callback<ElevationCallback> _callback);
}
