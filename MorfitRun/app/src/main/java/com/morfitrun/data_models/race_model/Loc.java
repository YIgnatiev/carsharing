package com.morfitrun.data_models.race_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vasia on 17.03.2015.
 */
public class Loc implements Serializable {

    @SerializedName("long")
    private double longitude;
    @SerializedName("lat")
    private double latitude;

    public Loc(double _latitude, double _longitude) {
        longitude = _longitude;
        latitude = _latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
