package youdrive.today.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/19/15.
 */
public class Coord {
    private double latitude;

    private double longitude;

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }


}
