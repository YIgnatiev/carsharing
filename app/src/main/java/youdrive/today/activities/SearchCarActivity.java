package youdrive.today.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.yandex.metrica.YandexMetrica;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar.OnProgressChangeListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.adapters.ProfileAdapter;
import youdrive.today.databinding.ActivitySearchCarBinding;
import youdrive.today.databinding.HeaderProfileBinding;
import youdrive.today.helpers.PreferenceHelper;
import youdrive.today.interceptors.ProfileInteractorImpl;
import youdrive.today.interceptors.SearchInteractorImpl;
import youdrive.today.listeners.ProfileActionListener;
import youdrive.today.listeners.SearchActionListener;
import youdrive.today.models.Menu;
import youdrive.today.models.User;
import youdrive.today.response.SearchCarResponse;
import youdrive.today.view.RadiusView;


public class SearchCarActivity extends BaseActivity implements ProfileActionListener,SearchActionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivitySearchCarBinding b;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private User mUser;
    private ProfileInteractorImpl mProfileInteractor;
    private SearchInteractorImpl mSearchCarInteractor;
    DiscreteSeekBar Radius;
    private float mZoomLevel;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker mMarker;
    private boolean isFake = false;
    private RadiusView radiusCircle;
    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_search_car);
        b.setListener(this);
        b.toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        setSupportActionBar(b.toolbar);
        setUpMapIfNeeded();
        createLocationRequest();
        mZoomLevel = mMap.getMinZoomLevel();
        if (App.getInstance().getPreference() != null) {
            if (App.getInstance().getPreference().getUser() != null) {
                mUser = new Gson().fromJson(App.getInstance().getPreference().getUser(), User.class);
            }
        }
        HeaderProfileBinding headerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_profile, null, false);
        if (mUser != null) {
            headerBinding.txtName.setText(mUser.getName());
        }
        b.lvProfile.addHeaderView(headerBinding.getRoot());
        b.lvProfile.setAdapter(new ProfileAdapter(this, R.layout.item_profile, getMenu()));

        b.drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        mProfileInteractor = new ProfileInteractorImpl();
        buildGoogleApiClient();
        checkInternet();
        Radius = b.Radius;
        setRadiusSeekBar();
        radiusCircle=b.RadiusView;
        mSearchCarInteractor = new SearchInteractorImpl();

b.btnDelete.setVisibility(View.GONE);

    }


    private void set_radius(int value){
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
        float mapWidthpixel =  mMapn.getView().getWidth()/2;
        float pixelonmeter=mapWidthpixel/mapWidthmeter;
        radiusCircle.setCircleRadius(Math.round((value*pixelonmeter)/2));
        System.out.println(mapWidthpixel);
        System.out.println(mapWidthmeter);
        System.out.println(pixelonmeter);
        System.out.println(value*pixelonmeter);


    }


    private void setRadiusSeekBar() {
        Radius.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                b.tvRadius.setText(String.valueOf(value)+" м");
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
                        int value= Radius.getProgress();
                        set_radius( value);

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
            int radius =Radius.getProgress();
            VisibleRegion vr = mMap.getProjection().getVisibleRegion();
           mSearchCarInteractor.postSearchCars(vr.latLngBounds.getCenter().latitude,vr.latLngBounds.getCenter().longitude, radius , this);
            b.btnSearch.setEnabled(false);

        }
    }

    public void onDelete(View v) {
        if (mMap != null) {
             mSearchCarInteractor.deleteSearchCars(this);
            b.btnDelete.setEnabled(false);

           // mSearchCarInteractor.getSearchCar(this);
             }
    }

    //listener
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 1:
                openUrl("https://youdrive.today/profile.html");
                break;
            case 2:
                openUrl("http://youdrive.today/tariffs-regulations.html");
                break;
            case 3:
                openUrl("http://youdrive.copiny.com/");
                break;
            case 4:
                call();
                break;
            case 5:
                App.getInstance().getPreference().clear();
                mProfileInteractor.logout(this);
                break;
        }
    }
    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
    private void call() {
        checkPhonePermission(() -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+74993223875"))));
    }
    @Override
    public void onLogout() {
        Timber.tag("Action").d("Logout");
        startActivity(new Intent(this, LoginActivity.class));
        new PreferenceHelper(this).clear();
    }


    @Override
    public void onError() {
        animateCamera(new LatLng(55.749792, 37.632495));

        Timber.tag("Error").d("Internal Error");
        String text = getString(R.string.internal_error);


    }

    @Override
    public void onSuccess(SearchCarResponse search, int type) {
        switch (type) {
            case 1:
                b.btnSearch.setEnabled(true);
                b.btnSearch.setVisibility(View.GONE);
                b.btnDelete.setVisibility(View.VISIBLE);
                String text="Message from API";
                showalert(text);
                break;
            case 2:
                break;
            case 3:
                b.btnDelete.setEnabled(true);
                b.btnSearch.setVisibility(View.VISIBLE);
                b.btnDelete.setVisibility(View.GONE);
                break;

        }
    }

    private void showalert(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchCarActivity.this);
        builder.setTitle(text)
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
    public void onSessionNotFound() {
        mProfileInteractor.logout(this);
    }

    @Override
    public void onInvalidRequest() {
//TODO Что делать при этой ошибке
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

    private List<Menu> getMenu() {
        List<Menu> items = new ArrayList<>();
        items.add(new Menu(R.drawable.icon_help, getString(R.string.profile)));
        items.add(new Menu(R.drawable.icon_tariff, getString(R.string.tariff)));
        items.add(new Menu(R.drawable.icon_help, getString(R.string.help)));
        items.add(new Menu(R.drawable.icon_call, getString(R.string.call)));
        items.add(new Menu(R.drawable.icon_exit, getString(R.string.exit)));

        return items;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                b.drawer.openDrawer(Gravity.LEFT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates() {
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
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPosition, 15);
//        mMap.animateCamera(cameraUpdate);

    }
    void updateLocation(Location location) {
        if (mMarker == null) {
            buildUserLocation(location);
        } else {
            mMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15);
//            mMap.animateCamera(cameraUpdate);
        }
    }
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

        }
    }
    @Override
    public void onConnected(Bundle bundle) {
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


}
