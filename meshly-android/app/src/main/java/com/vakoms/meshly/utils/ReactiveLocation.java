package com.vakoms.meshly.utils;

import android.content.Context;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/5/15.
 */
public class ReactiveLocation {
    private final int LOCATION_TIMEOUT_IN_SECONDS = 3;
    private final int MAX_ADDRESSES = 1;
    private final int LOCATION_UPDATE_INTERVAL = 60000;
    private final float SUFFICIENT_ACCURACY = 0.7f;
    private ReactiveLocationProvider locationProvider;
    private static ReactiveLocation INSTANCE;



    public static  ReactiveLocation getInstance (Context context) {

        if(INSTANCE == null)INSTANCE = new ReactiveLocation(context);
        return INSTANCE;
    }



    private ReactiveLocation(Context context){
        locationProvider = new ReactiveLocationProvider(context);

    }


    public Observable<List<Address>> reverseGeoCoding(Location location) {
        return locationProvider
                .getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), MAX_ADDRESSES)
                .subscribeOn(Schedulers.io())               // use I/O thread to query for addresses
                .observeOn(AndroidSchedulers.mainThread()); // return result in main android thread to manipulate UI


    }

    public Observable<List<Address>> geoCoding(String query) {


        return locationProvider
                .getGeocodeObservable(query, MAX_ADDRESSES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public Observable<Location> subscribeForLocationUpdates() {
        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);

        return locationProvider.getUpdatedLocation(request);
    }

    public Observable<Location> getFastLocation() {

        LocationRequest req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setExpirationDuration(TimeUnit.SECONDS.toMillis(LOCATION_TIMEOUT_IN_SECONDS))
                .setInterval(LOCATION_UPDATE_INTERVAL);

        return locationProvider.getUpdatedLocation(req)
                .filter(location -> location.getAccuracy() < SUFFICIENT_ACCURACY)
                .timeout(LOCATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, Observable.just((Location) null), AndroidSchedulers.mainThread())
                .first()
                .observeOn(AndroidSchedulers.mainThread());

    }
}
