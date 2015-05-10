package youdrive.today;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class Car implements Comparable<Car>{

    @SerializedName("car_id")
    String id;
    String model;
    String number;
    String color;
    float lat;
    float lon;
    int walktime;
    String transmission;
    int fuel;
    Status status;

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

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Car{" +
                "walktime=" + walktime +
                '}';
    }

    @Override
    public int compareTo(Car car) {
        return walktime - car.getWalktime();
    }
}
