package com.morfitrun.api.services;

import com.morfitrun.data_models.race_model.*;
import retrofit.Callback;
import retrofit.http.*;

import java.util.List;

/**
 * Created by vasia on 17.03.2015.
 */
public interface RaceService {
    @GET("/race/my")
    public void getAllRace(Callback<List<Race>> _callback);

    @GET("/race/{id}")
    public void getRace(@Path("id") String _id, Callback<RaceFullModel> _callback);

    @POST("/race")
    public void addRace(@Body Race _race, Callback<RaceId> _callback);

    @DELETE("/race/{id}")
    public void deleteRace(@Path("id") String _id, Callback<Race> _callback);

    @POST("/race/points/")
    public void addPoints(@Body PostPoints _postPoints,  Callback<PostPoints> _callback);
}
