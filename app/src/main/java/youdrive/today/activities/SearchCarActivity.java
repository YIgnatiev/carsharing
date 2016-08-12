package youdrive.today.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.yandex.metrica.YandexMetrica;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.OnProgressChangeListener;

import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivitySearchCarBinding;
import youdrive.today.interceptors.SearchInteractorImpl;
import youdrive.today.listeners.SearchActionListener;
import youdrive.today.response.SearchCarResponse;
import youdrive.today.view.RadiusView;


public class SearchCarActivity extends BaseActivity implements SearchActionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    private ActivitySearchCarBinding b;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private SearchInteractorImpl mSearchCarInteractor;
    DiscreteSeekBar Radius;
    private float mZoomLevel;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker mMarker;
    private boolean isFake = false;
    private RadiusView radiusCircle;
    private Circle mCircle;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_search_car);
        b.setListener(this);
        setSupportActionBar(b.toolbar);
        b.toolbar.setNavigationIcon(R.drawable.ic_back);
        b.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpMapIfNeeded();
        createLocationRequest();

        buildGoogleApiClient();
        checkInternet();
        Radius = b.Radius;

        radiusCircle = b.RadiusView;
        mSearchCarInteractor = new SearchInteractorImpl();
        mSearchCarInteractor.getSearchCar(this);
        b.btnDelete.setVisibility(View.GONE);
        b.btnSearch.setVisibility(View.GONE);
        radiusCircle.setVisibility(View.GONE);
        Radius.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void set_radius(int value) {
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        double left = vr.latLngBounds.southwest.longitude;
        Location center = new Location("center");
        center.setLatitude(vr.latLngBounds.getCenter().latitude);
        center.setLongitude(vr.latLngBounds.getCenter().longitude);
        Location middleLeftCornerLocation = new Location("center");
        middleLeftCornerLocation.setLatitude(center.getLatitude());
        middleLeftCornerLocation.setLongitude(left);
        float mapWidthmeter = center.distanceTo(middleLeftCornerLocation);
        SupportMapFragment mMapn = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        float mapWidthpixel = mMapn.getView().getWidth() / 2;
        float pixelonmeter = mapWidthpixel / mapWidthmeter;
        radiusCircle.setCircleRadius(Math.round((value * pixelonmeter) / 2));


    }


    private void setRadiusSeekBar() {
        Radius.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                b.tvRadius.setText(String.valueOf(value) + " м");
                set_radius(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }


        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        int value = Radius.getProgress();
                        set_radius(value);

                    }
                });
            }
        });
    }

    private void checkInternet() {
        if (!isNetworkConnected()) {
            showToast("Нет подключения к интернету");
            animateCamera(new LatLng(55.749792, 37.632495));
        }
    }

    private void animateCamera(LatLng position) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 11);
        mMap.animateCamera(cameraUpdate);
    }
    // listener

    public void onZoomIn(View v) {
        if (mMap != null) {

            mZoomLevel = mMap.getCameraPosition().zoom;
            if (mZoomLevel < mMap.getMaxZoomLevel()) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(++mZoomLevel));
            }
        }
    }

    public void onZoomOut(View v) {
        if (mMap != null) {

            mZoomLevel = mMap.getCameraPosition().zoom;
            if (mZoomLevel > mMap.getMinZoomLevel()) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(--mZoomLevel));
            }
        }
    }

    public void onSearch(View v) {
        if (mMap != null) {
            int radius = Radius.getProgress();
            VisibleRegion vr = mMap.getProjection().getVisibleRegion();
            double lat = vr.latLngBounds.getCenter().latitude;
            double lon = vr.latLngBounds.getCenter().longitude;
            mSearchCarInteractor.postSearchCars(lat, lon, radius, this);
            b.btnSearch.setEnabled(false);
            radiusCircle.setVisibility(View.GONE);
            Radius.setVisibility(View.GONE);
            drawSearchCircle(new LatLng(lat, lon), radius);

        }
    }

    public void onDelete(View v) {
        if (mMap != null) {
            mSearchCarInteractor.deleteSearchCars(this);
            b.btnDelete.setEnabled(false);
        }
    }

    public void onBack(View v) {

    }


    @Override
    public void onError() {
        animateCamera(new LatLng(55.749792, 37.632495));
        Timber.tag("Error").d("Internal Error");
    }

    @Override
    public void onSuccess(SearchCarResponse search, int type) {
        switch (type) {
            case 1:
                enableDeleteButton();
                String text=search.getText();
                showalert(text);
                break;
            case 2:
                Float lat = search.getLatResponse();
                Float lon = search.getLonResponse();
                int rad = search.getRadiusResponse();
                if (lat != null && lon != null && rad != 0) {
                    enableDeleteButton();
                    drawSearchCircle(new LatLng(lat, lon), rad);
                    b.tvRadius.setText(String.valueOf(rad) + " м");

                } else {
                    enableSearchButton();
                }
                break;
            case 3:
                enableSearchButton();
                break;

        }
    }

    public void enableDeleteButton() {
        b.btnDelete.setEnabled(true);
        b.btnDelete.setVisibility(View.VISIBLE);
        b.btnSearch.setVisibility(View.GONE);
        radiusCircle.setVisibility(View.GONE);
        Radius.setVisibility(View.GONE);
    }

    public void enableSearchButton() {
        b.btnSearch.setEnabled(true);
        radiusCircle.setVisibility(View.VISIBLE);
        Radius.setVisibility(View.VISIBLE);
        b.btnSearch.setVisibility(View.VISIBLE);
        b.btnDelete.setVisibility(View.GONE);
        if (mCircle != null) {
            mCircle.remove();
        }
    }

    private void drawSearchCircle(LatLng position, double radius) {
        CircleOptions circleOptions = new CircleOptions().center(position).radius(radius / 2).fillColor(getResources().getColor(R.color.maintransparent)).strokeColor(getResources().getColor(R.color.maintransparent)).strokeWidth(1);
        mCircle = mMap.addCircle(circleOptions);
        System.out.println(position + " " + radius);
    }

    private void showalert(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchCarActivity.this);
        builder.setMessage(text)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onAccessDenied(String text) {
        Timber.tag("Error").e("onAccessDenied");
    }

    @Override
    public void onCommandNotSupported(String text) {
        Timber.tag("Error").e("onCommandNotSupported");
    }

    @Override
    public void onSessionNotFound(String text) {
        Timber.tag("Error").e("onSessionNotFound");


    }


    @Override
    public void onUnknownError(String text) {

    }

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void buildUserLocation(Location location) {
        LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions()
                .position(myPosition)
                .title("Это Вы!")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location));
        mMarker = mMap.addMarker(options);
        if (isFake) mMarker.setVisible(false);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPosition, 15);
        mMap.animateCamera(cameraUpdate);

    }

    void updateLocation(Location location) {
        if (mMarker == null) {
            buildUserLocation(location);
        } else {
            mMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
            mMap.animateCamera(cameraUpdate);
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
        if (mLastLocation != null) {
            updateLocation(mLastLocation);


        } else {
            mLastLocation = new Location("");
            mLastLocation.setLatitude(55.749792);
            mLastLocation.setLongitude(37.632495);// Create moscow coordinates;
            animateCamera(new LatLng(55.749792, 37.632495));
            isFake = true;
            updateLocation(mLastLocation);

            showToast("Не удалось определить месторасположение");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("map_0_0");
        App.tracker().setScreenName("map_0_0");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());
        mGoogleApiClient.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (mGoogleApiClient.isConnected()) startLocationUpdates();

        //startUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            // stopLocationUpdates();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        // if (timerSubscription != null) timerSubscription.unsubscribe();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //DO WHATEVER YOU WANT WITH GOOGLEMAP

        if (mMap != null) {
            return;
        }
        mMap = googleMap;

        setUpMapIfNeeded();
        setRadiusSeekBar();
        mZoomLevel = mMap.getMinZoomLevel();


    }
}
