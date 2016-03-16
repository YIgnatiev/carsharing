package pro.theboard.utils;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pro.theboard.constants.Constants;

/**
 * Created by Oleh Makhobey on 16.07.2015.
 * tajcig@ya.ru
 */

    public class LocationReceiver implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
            LocationListener{

        private static final String LOCATION = "LocationReceiver";
        private Context mContext;
        private LocationRequest mLocationRequest;
        private GoogleApiClient mClient;
        private NewLocationListener mLocationListener;

        public LocationReceiver(Context _context){
            this.mContext = _context;
            initClient();
            setLocationRequest();

        }



        private void initClient() {
            mClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


        }
        public boolean isConnected(){
            return mClient.isConnected();
        }

        public boolean isConecting(){
            return mClient.isConnecting();
        }


        private void setLocationRequest(){
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(Constants.TIME_SECOND * 30);
            mLocationRequest.setFastestInterval(Constants.TIME_MINUTE );
            mLocationRequest.setSmallestDisplacement(0);
        }



        public void connect(){
            if(!mClient.isConnected())mClient.connect();
        }

        public void disconnect(){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mClient, this);
            if(mClient.isConnected())mClient.disconnect();
        }



        public void setLocationUpdateListener( NewLocationListener _listener){
            this.mLocationListener= _listener;
        }

        private void startLocationUpdate(){
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient,mLocationRequest,this);
        }



        public Location getLastLocation(){
            return LocationServices.FusedLocationApi.getLastLocation(mClient);
        }
        //google callbacks
        @Override
        public void onConnected(Bundle bundle) {
            Location location = getLastLocation();
            if(location != null){
                saveAndUpdateLocationOnServer(location);
                if(mLocationListener != null)mLocationListener.onLocationUpdated(location);
            }else{
                startLocationUpdate();
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            mLocationListener.onLocationFailed();
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            mLocationListener.onLocationFailed();
        }
        //custom listener



        public boolean isGpsEnabled() {

            LocationManager    locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = false;
            try {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            } catch (Exception ex) {
                Log.e(LOCATION, ex.getMessage());
            }


            return isGpsEnabled;
        }



        public void saveAndUpdateLocationOnServer(Location location) {

            if(mLocationListener != null)mLocationListener.onLocationUpdated(location);

        }




        @Override
        public void onLocationChanged(Location location) {
            saveAndUpdateLocationOnServer(location);
            if(mLocationListener!=null)mLocationListener.onLocationUpdated(location);
        }
    }


