package com.morfitrun.api.google_api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by vasia on 23.03.2015.
 */
public class GoogleRestClient {

    public static final String URL = "http://maps.googleapis.com/maps/api/";
    private ElevationService mElevationService;
    private static GoogleRestClient mInstance;

    private GoogleRestClient(){
        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(URL)
                .setConverter(new GsonConverter(gson))
                .build();
        mElevationService = restAdapter.create(ElevationService.class);
    }

    public static final GoogleRestClient getInstance(){
        if (mInstance == null)
            mInstance = new GoogleRestClient();
        return mInstance;
    }

    public final ElevationService getElevationService(){
        return mElevationService;
    }

}
