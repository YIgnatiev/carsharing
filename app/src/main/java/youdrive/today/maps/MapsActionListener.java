package youdrive.today.maps;

import java.util.List;

import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Status;

/**
 * Created by psuhoterin on 22.04.15.
 */
public interface MapsActionListener {

    void onError();

    void onCars(List<Car> cars);

    void onForbidden();

    void onTariffNotFound();

    void onCar(Car car);

    void onStatus(Status status);

    void onCheck(Check check);

    void onUnknownError(String text);
}
