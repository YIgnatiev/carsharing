package youdrive.today.response;

import java.util.ArrayList;

import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Status;

/**
 * Created by psuhoterin on 18.05.15.
 */
public class CarsResponse {

    private String status;
    private ArrayList<Car> cars;
    private Car car;
    private boolean success;
    private int code;
    private String message;
    private Check check;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public boolean isSuccess() {
        return success;
    }

    public Car getCar() {
        return car;
    }

    public Check getCheck() {
        return check;
    }
}
