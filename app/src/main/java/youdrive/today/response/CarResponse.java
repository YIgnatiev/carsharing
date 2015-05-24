package youdrive.today.response;

import java.util.ArrayList;

import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Status;

public class CarResponse extends BaseResponse {

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
}
