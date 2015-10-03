package youdrive.today.interceptors;

import youdrive.today.listeners.MapsActionListener;
import youdrive.today.listeners.PolygonListener;

/**
 * Created by psuhoterin on 22.04.15.
 */
public interface MapsInteractor {

    void getStatusCars(double lat, double lon, MapsActionListener listener);
    void getStatusCar(MapsActionListener listener);
    void getInfo(PolygonListener listener);
}
