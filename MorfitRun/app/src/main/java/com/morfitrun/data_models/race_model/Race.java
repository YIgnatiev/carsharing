package com.morfitrun.data_models.race_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vasia on 11.03.2015.
 */
public class Race implements Serializable {

    @SerializedName("_id")
    private String id;
    private String title;
    private String description;
    private String startDate;
    private String deadline;
    private String repeat;
    private int limit;

    public Race(String _id, String _title, String _description, String _startDate, String _deadline, String _repeat, int _limit) {
        id = _id;
        title = _title;
        description = _description;
        startDate = _startDate;
        deadline = _deadline;
        repeat = _repeat;
        limit = _limit;
    }

    public Race(String _title, String _description, String _startDate, String _deadline, String _repeat, int _limit) {
        title = _title;
        description = _description;
        startDate = _startDate;
        deadline = _deadline;
        repeat = _repeat;
        limit = _limit;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getRepeat() {
        return repeat;
    }

    public int getLimit() {
        return limit;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStarDate() {
        return startDate;
    }

}
