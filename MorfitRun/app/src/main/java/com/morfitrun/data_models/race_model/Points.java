package com.morfitrun.data_models.race_model;

import java.io.Serializable;

/**
 * Created by vasia on 17.03.2015.
 */
public class Points implements Serializable{

    private Loc loc;
    private double elevation;
    private String photo;

    public Points(double _elevation, String _photo, double _latitude, double _longitude) {
        elevation = _elevation;
        photo = _photo;
        loc = new Loc(_latitude, _longitude);

    }

    public double getLongitude(){
        return loc.getLongitude();
    }

    public double getLatitude(){
        return loc.getLatitude();
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public String getPhoto() {
        return photo;
    }
}
