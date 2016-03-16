package com.vakoms.meshly.models;

/**
 * Created by taras.melko on 8/19/14.
 */
public class Geo {

    private Double lat;
    private Double lng;

    public Geo(double lat,double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
