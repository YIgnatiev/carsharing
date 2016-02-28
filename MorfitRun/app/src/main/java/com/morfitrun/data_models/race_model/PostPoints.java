package com.morfitrun.data_models.race_model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vasia on 23.03.2015.
 */
public class PostPoints {

    private Points[] points;
    @SerializedName("race")
    private String id;

    public PostPoints(Points[] _points, String _id) {
        this.points = _points;
        this.id = _id;
    }

    public Points[] getPoints() {
        return points;
    }

    public void setPoint (Points[] _points) {
        this.points = _points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
