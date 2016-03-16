package com.vakoms.meshly.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import meshly.vakoms.com.meshly.R;

/**
 * Created by taras.melko on 8/18/14.
 */
public class GeocoderUtil {

    Context activity;
    private static final String TAG = "Geocoder Util";











    public GeocoderUtil(Context context) {
        activity = context;
    }

    public static double getDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {


        float[] result = new float[1];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);

        return result[0];
    }

    public static String getFormattedDistance(List<Double> _geo){

//        double firstLat = _geo.get(0);
//        double firstLon =_geo.get(1);
//        double secondLat = P.GPS.getLatitude();
//        double secondLon =  P.GPS.getLongitude();

        Location location = new Location("");
        location.setLatitude(_geo.get(1));
        location.setLongitude(_geo.get(0));

        Location myLocation = P.GPS.getLocation();

        double distance=  myLocation.distanceTo(location);
        //double distance = GeocoderUtil.getDistance(firstLat, firstLon, secondLat, secondLon);
        String stringDistance;
        if (distance > 1000) {
            stringDistance =  (int)distance/1000 + " km";
            return stringDistance ;
        } else {

            return "nearby";
        }
    }


    public static float getDistacne(List<Double> _geo){

            Location location = new Location("");
            location.setLatitude(_geo.get(1));
            location.setLongitude(_geo.get(0));
            Location myLocation = P.GPS.getLocation();
           return myLocation.distanceTo(location);

    }




    public static String getCompleteAddress(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return ("error");
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String adressText = "";
            adressText = addresses.get(0).getAddressLine(1).replaceAll("\\d", "") + ", " + address.getCountryName();
            return adressText;
        } else {
            return context.getString(R.string.fragment_settings_CantFindLocation);
        }
    }


}