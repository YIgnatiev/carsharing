package youdrive.today.car;

import youdrive.today.maps.MapsActionListener;

/**
 * Created by psuhoterin on 26.04.15.
 */
public interface CarInteractor {

    void order(String id, double lat, double lon, CarActionListener listener);
    void open(CarActionListener listener);
    void result(String token, final CarActionListener listener);

}
