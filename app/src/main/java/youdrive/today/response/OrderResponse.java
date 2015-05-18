package youdrive.today.response;

import com.google.gson.annotations.SerializedName;

import youdrive.today.Car;

/**
 * Created by psuhoterin on 18.05.15.
 */
public class OrderResponse {

    @SerializedName("booking_time_left")
    private int bookingTimeLeft;
    private Car car;
    private boolean success;
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getBookingTimeLeft() {
        return bookingTimeLeft;
    }

    public Car getCar() {
        return car;
    }

    public boolean isSuccess() {
        return success;
    }
}
