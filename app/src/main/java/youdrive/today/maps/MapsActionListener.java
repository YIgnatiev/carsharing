package youdrive.today.maps;

import java.util.List;

import youdrive.today.Car;

/**
 * Created by psuhoterin on 22.04.15.
 */
public interface MapsActionListener {

    void onError();

    void onCars(List<Car> cars);

    void onForbidden();

    void onTariffNotFound();
}
