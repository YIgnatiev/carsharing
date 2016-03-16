package com.morfitrun.global;

import android.content.Context;
import android.graphics.Color;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.morfitrun.data_models.race_model.Points;

/**
 * Created by vasia on 23.03.2015.
 */
public class RaceDrawer {
    private Context mContext;
    private Points[] mPoints;
    private GoogleMap mGoogleMaps;

    public RaceDrawer(final Context _context, final Points[] _points, final GoogleMap _googleMaps){
        mPoints = _points;
        mGoogleMaps = _googleMaps;
        mContext = _context;
    }

    public void drawPrimaryLinePath(){
        if (mPoints == null || mPoints.length < 2)
            return ;
        PolylineOptions options = new PolylineOptions();
        options.color(Color.parseColor("#CC0000FF"));
        options.width(5);
        options.visible( true );
        for (int i = 1; i < mPoints.length; i++) {
            options.add(new LatLng(mPoints[i-1].getLatitude(), mPoints[i-1].getLongitude()));
            options.add(new LatLng(mPoints[i].getLatitude(), mPoints[i].getLongitude()));
        }
        mGoogleMaps.addPolyline(options);
    }

    public void cameraMoveTo(){
        if (mPoints == null || mPoints.length < 1)
            return;
        MapsInitializer.initialize(mContext);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mPoints[0].getLatitude(), mPoints[0].getLongitude()))
                .zoom(15)
                .build();
        mGoogleMaps.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}

