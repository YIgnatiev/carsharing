package pro.theboard.utils;

import android.location.Location;

/**
 * Created by Oleh Makhobey on 16.07.2015.
 * tajcig@ya.ru
 */
public interface NewLocationListener {


        void onLocationUpdated(Location _location);

        void onLocationFailed();

}
