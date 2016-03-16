package com.morfitrun.data_models.elevation_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vasia on 23.03.2015.
 */
public class LatLng {

    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
