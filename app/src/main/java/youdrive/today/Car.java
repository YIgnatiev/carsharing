package youdrive.today;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class Car {

    @SerializedName("region_id")
    String id;
    String model;
    String number;
    String color;
    float lat;
    float lon;
    int walktime;
    String transmission;
    int fuel;

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public int getWalktime() {
        return walktime;
    }

    public String getTransmission() {
        return transmission;
    }

    public int getFuel() {
        return fuel;
    }

    @Override
    public String toString() {
        return "Car{" +
                "car_id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", number='" + number + '\'' +
                ", color='" + color + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", walktime=" + walktime +
                ", transmission='" + transmission + '\'' +
                ", fuel=" + fuel +
                '}';
    }
}
