package youdrive.today.models;

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
    Integer fuel;
    int distance;
    Tariff tariff;
    String img;
    String pointer_resource;
    int discount;
    boolean transferable;
    @SerializedName("in_transfer")
    boolean inTransfer;

    public Car(String id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPointer_resource() {
        return pointer_resource;
    }

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

    public Integer getFuel() {
        return fuel;
    }

    public int getDistance() {
        return distance;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public boolean isInTransfer() {
        return inTransfer;
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
        dest.writeFloat(lat);
        dest.writeFloat(lon);
    }

    protected Car(Parcel in) {
        model = in.readString();
        number = in.readString();
        color = in.readString();
        img = in.readString();
        lat = in.readFloat();
        lon = in.readFloat();
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

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", number='" + number + '\'' +
                ", color='" + color + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", walktime=" + walktime +
                ", transmission='" + transmission + '\'' +
                ", fuel=" + fuel +
                ", distance=" + distance +
                ", tariff=" + tariff +
                ", img='" + img + '\'' +
                ", pointer_resource='" + pointer_resource + '\'' +
                ", discount=" + discount +
                ", transferable=" + transferable +
                ", inTransfer=" + inTransfer +
                '}';
    }
}
