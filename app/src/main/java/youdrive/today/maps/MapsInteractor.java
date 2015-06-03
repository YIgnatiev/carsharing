package youdrive.today.maps;

/**
 * Created by psuhoterin on 22.04.15.
 */
public interface MapsInteractor {

    void getStatusCars(double lat, double lon, MapsActionListener listener);
    void getStatusCar(MapsActionListener listener);
    void updateCar(MapsActionListener listener);

}
