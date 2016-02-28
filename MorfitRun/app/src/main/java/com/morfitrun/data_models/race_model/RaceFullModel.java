package com.morfitrun.data_models.race_model;

/**
 * Created by vasia on 19.03.2015.
 */
public class RaceFullModel {

    private Race race;
    private Points points[];

    public RaceFullModel(Race race, Points points[]) {
        this.race = race;
        this.points = points;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Points[] getPoints() {
        return points;
    }

    public void setPoints(Points points[]) {
        this.points = points;
    }
}
