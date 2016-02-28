package com.vakoms.meshly.interfaces;

import android.location.Location;

/**
 * Created by Oleh Makhobey on 01.06.2015.
 * tajcig@ya.ru
 */
public interface NewLocationListener {

    void onLocationUpdated(Location _location);

    void onLocationFailed();
}
