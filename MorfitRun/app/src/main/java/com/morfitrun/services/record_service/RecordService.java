package com.morfitrun.services.record_service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.morfitrun.R;
import com.morfitrun.api.google_api.GoogleRestClient;
import com.morfitrun.data_models.elevation_model.Coordinates;
import com.morfitrun.data_models.elevation_model.ElevationCallback;
import com.morfitrun.data_models.race_model.Points;
import com.morfitrun.fragments.RecordFragment;
import com.morfitrun.global.Stopwatch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by vasia on 24.03.2015.
 */
public class RecordService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Stopwatch mTimer;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double mPreviousLatitude = -1;
    private double mPreviousLongitude = -1;
    private float mDistance = 0;
    private RecordFragment mRecordFragment;
    private ArrayList<Points> mPointsData;
    private Notification.Builder mNotification;
    private String mCurrentFormatElevation;
    private boolean isRecordRun = false;
    private String mRaceId;

    @Override
    public void onCreate() {
        super.onCreate();
        startNotification();
        initLocation();
        if (mPointsData == null)
            mPointsData = new ArrayList<>();
        if (mTimer == null) {
            initTimer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        final RecordServiceBinder mBinder = new RecordServiceBinder(this);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mRecordFragment = null;
        mTimer.setTimeView(null);
        return true;
    }

    public void setRecordFragment(RecordFragment _recordFragment){
        mRecordFragment = _recordFragment;
    }

    private void initTimer(){
        mTimer = new Stopwatch(null);
    }

    public void setTimerTextView(TextView _tv){
        mTimer.setTimeView(_tv);
    }

    public void setRaceId(final String _raceId){
        mRaceId = _raceId;
    }

    public String getRaceId(){
        return mRaceId;
    }

    public void startPauseTimer(){
        if (!isRecordRun)
            isRecordRun = true;
        if (mTimer.isTimerRunning()) {
            mTimer.pause();
            stopLocationUpdate();
        } else {
            mTimer.start();
            startLocationUpdate();
        }
    }

    public boolean isTimerRunning(){
        return mTimer.isTimerRunning();
    }

    public boolean isRecordRuning(){
        return isRecordRun;
    }

    public ArrayList<Points> getAllPoints(){
        return mPointsData;
    }

    public String getCurrentElevation(){
        if (mCurrentFormatElevation == null)
            return getString(R.string.elevation);
        return mCurrentFormatElevation;
    }

    public String getCurrentDistance(){
        if (mDistance == 0)
            return getString(R.string.distance);
        return getString(R.string.distance) + " (" + String.format("%1$,.2f", mDistance) + ")";
    }

    public void initLocation(){
        buildGoogleApiClient();
        createLocationRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void startLocationUpdate(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location _location) {
        final double latitude = _location.getLatitude();
        final double longitude = _location.getLongitude();

        if (mPreviousLatitude == -1 && mPreviousLongitude == -1){
            mPreviousLatitude = latitude;
            mPreviousLongitude = longitude;
        } else {
            calculateDistance(_location.getLatitude(), _location.getLongitude());
            if (mRecordFragment != null)
            mRecordFragment.drawPrimaryLinePath(latitude, longitude, mPreviousLatitude, mPreviousLongitude);
            mPreviousLatitude = latitude;
            mPreviousLongitude = longitude;
        }
        addPointsToArray(latitude, longitude);
        getElevation(latitude, longitude);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void calculateDistance(double _currentLatitude, double _currentLongitude){
        float[] results = new float[1];
        Location.distanceBetween(mPreviousLatitude, mPreviousLongitude, _currentLatitude, _currentLongitude, results);
        mDistance = mDistance + results[0];
        final String formatDistance = getString(R.string.distance) + " (" + String.format("%1$,.2f", mDistance) + ")";
        mNotification.setContentText(formatDistance);
        startForeground(1, mNotification.build());
        if (isConnect())
            mRecordFragment.setDistance(formatDistance);
    }

    private void addPointsToArray(final double _latitude, final double _longitude){
        mPointsData.add(new Points(0, null, _latitude, _longitude));
    }

    private void getElevation(final double _latitude, final double _longitude){
        final String location = "" + _latitude + "," + _longitude;
        final String sensor = "true_or_false";
        GoogleRestClient.getInstance().getElevationService().getElevation(location, sensor, new Callback<ElevationCallback>() {
            @Override
            public void success(ElevationCallback elevationCallback, Response response) {
                addElevationToPointsData(elevationCallback.getCoordinates());
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void addElevationToPointsData(Coordinates _coordinates){
        if (_coordinates == null)
            return;
        final double latitude = _coordinates.getLatitude();
        final double longitude = _coordinates.getLongitude();
        final double elevation = _coordinates.getElevation();

        final DecimalFormat df = new DecimalFormat("#.##");
        mCurrentFormatElevation = getString(R.string.elevation) + "(" + df.format(elevation) + ")";

        if (isConnect())
            mRecordFragment.setElevation(mCurrentFormatElevation);

        for (int i = mPointsData.size() - 1; i >= 0; i--){
            if (mPointsData.get(i).getLatitude() == latitude && mPointsData.get(i).getLongitude() == longitude){
                mPointsData.get(i).setElevation(elevation);
                break;
            }
        }
    }

    private boolean isConnect(){
        return mRecordFragment != null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startNotification(){
        mNotification =  new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(getString(R.string.record_track))
                .setContentText(getString(R.string.distance));
        startForeground(1, mNotification.build());
    }
}

