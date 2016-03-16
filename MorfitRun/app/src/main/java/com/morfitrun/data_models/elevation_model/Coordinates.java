package com.morfitrun.data_models.elevation_model;

/**
 * Created by vasia on 23.03.2015.
 */
public class Coordinates {

    private double elevation;
    private LatLng location;

    public double getElevation() {
        return elevation;
    }

    public double getLatitude(){
        return location.getLatitude();
    }

    public double getLongitude(){
        return location.getLongitude();
    }
}
