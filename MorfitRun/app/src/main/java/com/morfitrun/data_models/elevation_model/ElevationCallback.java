package com.morfitrun.data_models.elevation_model;

/**
 * Created by vasia on 23.03.2015.
 */
public class ElevationCallback {

    private Coordinates[] results;
    private String status;

    public Coordinates getCoordinates(){
        if (results.length == 0)
            return null;
        return results[0];
    }

    public String getStatus() {
        return status;
    }
}
