package youdrive.today.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Status;

public class CarResponse extends BaseResponse {

    @SerializedName("booking_time_left")
    private int bookingTimeLeft;

    private String status;
    private ArrayList<Car> cars;
    private Car car;
    private Check check;

    public String getStatus() {
        return status;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public Car getCar() {
        return car;
    }

    public Check getCheck() {
        return check;
    }

    public int getBookingTimeLeft() {
        return bookingTimeLeft;
    }
}
