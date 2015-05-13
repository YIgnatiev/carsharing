package youdrive.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class Car implements Comparable<Car>, Parcelable {

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
    int distance;
    Tariff tariff;
    String img;

    public String getImg() {
        return img;
    }

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

    public int getDistance() {
        return distance;
    }

    public Tariff getTariff() {
        return tariff;
    }

    @Override
    public int compareTo(Car car) {
        return walktime - car.getWalktime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeString(number);
        dest.writeString(color);
        dest.writeString(img);
    }

    protected Car(Parcel in) {
        model = in.readString();
        number = in.readString();
        color = in.readString();
        img = in.readString();
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
