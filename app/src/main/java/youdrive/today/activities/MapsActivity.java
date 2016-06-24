package youdrive.today.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.adapters.ProfileAdapter;
import youdrive.today.databinding.ActivityMapsBinding;
import youdrive.today.databinding.DialogCloseCar;
import youdrive.today.databinding.DialogInfo;
import youdrive.today.databinding.HeaderProfileBinding;
import youdrive.today.databinding.MarkerInfo;
import youdrive.today.databinding.OpenCarDialog;
import youdrive.today.databinding.PopupDistance;
import youdrive.today.helpers.AppUtils;
import youdrive.today.helpers.PreferenceHelper;
import youdrive.today.interceptors.CarInteractorImpl;
import youdrive.today.interceptors.MapsInteractorImpl;
import youdrive.today.interceptors.ProfileInteractorImpl;
import youdrive.today.listeners.CarActionListener;
import youdrive.today.listeners.MapsActionListener;
import youdrive.today.listeners.PolygonListener;
import youdrive.today.listeners.ProfileActionListener;
import youdrive.today.models.Car;
import youdrive.today.models.Check;
import youdrive.today.models.Command;
import youdrive.today.models.Coord;
import youdrive.today.models.Menu;
import youdrive.today.models.Status;
import youdrive.today.models.User;
import youdrive.today.response.PolygonResponse;

import static youdrive.today.models.Status.BOOKING;
import static youdrive.today.models.Status.NORMAL;
import static youdrive.today.models.Status.PARKING;
import static youdrive.today.models.Status.USAGE;

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener, CarActionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, PolygonListener {

    private static final int RC_BOOK = 0;
    private static final int RC_CHECK = 1;
    private ActivityMapsBinding b;
    private DialogInfo bInfo;
    private OpenCarDialog bOpenCar;
    private DialogCloseCar bCloseCar;
    private String mToken;
    private Marker mMarker;
    private Car mCar;
    private Command mCommand;
    private GoogleMap mMap;
    private List<PolygonOptions> mPolygons;
    private ProfileInteractorImpl mProfileInteractor;
    private CarInteractorImpl mCarInteractor;
    private MapsInteractorImpl mMapsInteractor;
    private LocationRequest mLocationRequest;
    private float mZoomLevel;
    private Check mCheck;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int mBookingTimeLeft;
    private Status mStatus;
    private MaterialDialog mDialog;
    private User mUser;
    private Subscription timerSubscription;
    private boolean isShowCommandPopup = false;
    private boolean isShowClosePopup = false;
    private boolean isInfoPopup = false;
    private boolean isMoveCamera = false;
    private boolean isMoveCameraWithMe = false;

    private boolean isFake = false;

    private static final int REQUEST_CODE_FINE_LOCATION=222;


    private void startUpdates() {
        timerSubscription = Observable
                .interval(20, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadCars, (error) -> {
                });
    }


    private void loadCars(long seconds) {
        if (isProcessing()) {
            mMapsInteractor.clearSubscriptions();
            return;
        }

        if (mStatus != null && mLastLocation != null) {
            if (NORMAL.equals(mStatus) || BOOKING.equals(mStatus)) {
                if (isNetworkConnected())
                    mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
            } else {
                if (isNetworkConnected())
                    mMapsInteractor.getStatusCar(MapsActivity.this);
            }
        }
    }


    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        b.setListener(this);
        b.toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        setSupportActionBar(b.toolbar);
        setUpMapIfNeeded();
        createLocationRequest();
        if(mMap!=null) mZoomLevel = mMap.getMinZoomLevel();

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
        mCarInteractor = new CarInteractorImpl();
        mMapsInteractor = new MapsInteractorImpl();
        buildGoogleApiClient();
        checkInternet();
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
        if (mGoogleApiClient.isConnected()) startLocationWithCheckPermissions();

        startUpdates();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if (timerSubscription != null) timerSubscription.unsubscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationWithCheckPermissions();
                }/* else {

                    new AlertDialog.Builder(this)
                            .setMessage(R.string.need_location_permissions)
                            .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                                finish();
                            })
                            .setPositiveButton(R.string.accept, (dialogInterface, i) -> {
                                startLocationWithCheckPermissions();
                            }).show();
                }*/
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

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

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationWithCheckPermissions()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_FINE_LOCATION);
        }
        else
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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

    private void showCarsDialog(final Car car) {

        if (car == null) return;

        if (!NORMAL.equals(mStatus)) return;

        bInfo = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_info_contents, null, false);
        mCar = car;
        bInfo.setActivity(this);
        bInfo.setCar(car);
        bInfo.btnBook.setIndeterminateProgressMode(true);
        mDialog = new MaterialDialog
                .Builder(MapsActivity.this)
                .customView(bInfo.getRoot(), true)
                .widgetColorRes(R.color.white)
                .show();
    }


    HashMap<Marker, Car> mMarkerCar = new HashMap<>();

    private void addMarker(final Car car) {
        Glide.with(getApplicationContext())
                .load(car.getPointer_resource() + "_android.png")
                .asBitmap()
                .sizeMultiplier(0.5f)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        putMarker(car, BitmapDescriptorFactory.fromBitmap(resource));

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        putMarker(car, BitmapDescriptorFactory.fromResource(R.drawable.car_location));
                    }
                });
    }


    private void putMarker(Car car, BitmapDescriptor icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .flat(true)
                .position(new LatLng(car.getLat(), car.getLon()))
                .title(car.getModel())
                .icon(icon);

        mMarkerCar.put(mMap.addMarker(markerOptions), car);
        if (car.getDiscount() < 15) return;
        int resDiscount = R.drawable.ic_percent_orange;
        if (car.getDiscount() >= 50) {
            resDiscount = R.drawable.ic_percent_bordo;
        } else if (car.getDiscount() >= 25) {
            resDiscount = R.drawable.ic_percent_red;
        } else if (car.getDiscount() >= 15) {
            resDiscount = R.drawable.ic_percent_orange;
        }
        MarkerOptions markerOptionsPercent = new MarkerOptions()
                .flat(true)
                .position(new LatLng(car.getLat(), car.getLon()))
                .title(car.getModel())
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(resDiscount));

//        mMarkerCar.put(mMap.addMarker(markerOptionsPercent), car);





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
        unlock(text);
    }

    @Override
    public void onAccessDenied(String text) {
        Timber.tag("Error").e("onAccessDenied");
        unlock(text);
    }

    private void unlock(String text) {
        if (bOpenCar != null && bOpenCar.btnCancel != null && bOpenCar.btnCancel.getProgress() == 50) {
            AppUtils.error(text, bOpenCar.btnCancel);
            bOpenCar.btnOpen.setEnabled(true);

        } else if (bCloseCar != null && bCloseCar.btnCloseRent != null && bCloseCar.btnCloseRent.getProgress() == 50) {
            AppUtils.error(text, bCloseCar.btnCloseRent);
            bCloseCar.btnCloseOrOpen.setEnabled(true);

        } else if (bOpenCar != null && bOpenCar.btnOpen != null && bOpenCar.btnOpen.getProgress() == 50) {
            AppUtils.error(text, bOpenCar.btnOpen);
            bOpenCar.btnCancel.setEnabled(true);

        } else if (bCloseCar != null && bCloseCar.btnCloseOrOpen != null && bCloseCar.btnCloseOrOpen.getProgress() == 50) {
            AppUtils.error(text, bCloseCar.btnCloseOrOpen);
            bCloseCar.btnCloseRent.setEnabled(true);

        } else if (bInfo != null && bInfo.btnBook != null && bInfo.btnBook.getProgress() == 50) {
            AppUtils.error(text, bInfo.btnBook);
        }
    }

    @Override
    public void onCommandNotSupported(String text) {
        Timber.tag("Error").e("onCommandNotSupported");
        unlock(text);
    }

    @Override
    public void onClose() {
        mMapsInteractor.getStatusCar(this);
        onStatus(PARKING);
        Timber.tag("Action").d("onClose");
        if (bCloseCar.btnCloseOrOpen != null) {
            bCloseCar.btnCloseRent.setEnabled(true);
            AppUtils.success(bCloseCar.btnCloseOrOpen, getString(R.string.open_car));
        }
    }

    @Override
    public void onComplete(Check check) {
        Timber.tag("Action").d("onComplete " + check.toString());
        hideBottomWindow();
        startActivityForResult(new Intent(this, CompleteActivity.class).putExtra("check", check), RC_CHECK);
        mMapsInteractor.getStatusCar(this);
    }

    @Override
    public void onTransfer() {
        Timber.tag("Action").d("onTransfer ");
        if (bCloseCar != null) {
            if (mStatus == USAGE) {
                bCloseCar.btnCloseOrOpen.setEnabled(false);
                mCarInteractor.command(Command.CLOSE, MapsActivity.this);
            } else {
                bCloseCar.btnCloseRent.setVisibility(View.GONE);
                mMapsInteractor.getStatusCar(this);
            }
        }

        if(bOpenCar != null){
            bOpenCar.btnCancel.setEnabled(true);
            AppUtils.success(bOpenCar.btnCancel, getString(R.string.transfer_car));
            bOpenCar.btnCancel.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBookingTimeLeft(int bookingTimeLeft) {
        Timber.tag("Action").d("onBookingTimeLeft " + bookingTimeLeft);
        mBookingTimeLeft = bookingTimeLeft;

        for (Map.Entry<Marker, Car> entry : mMarkerCar.entrySet()) {
            if (entry.getValue().equals(mCar)) {
                entry.getKey().showInfoWindow();
            }
        }
    }

    @Override
    public void onSessionNotFound(String text) {
        Timber.tag("Error").e("onSessionNotFound");
        unlock(text);
    }

    private void clear() {
        mMarkerCar.clear();
        mMap.clear();
        mMarker.remove();
    }

    @Override
    public void onCars(List<Car> cars) {
        clear();
        if (mPolygons != null)
            for (PolygonOptions polygon : mPolygons)
                mMap.addPolygon(polygon);

        else mMapsInteractor.getInfo(this);
        Collections.sort(cars);
        if (!isMoveCameraWithMe) {
            isMoveCameraWithMe = true;
            onMoveCameraWithMe(cars.get(0));
        }
        for (Car c : cars) {
            addMarker(c);
        }
        showDistancePopup(cars.get(0).getWalktime());
    }

    private void onMoveCameraWithMe(final Car car) {
        if (mMarker != null) {
            if (isFake) animateCamera(mMarker.getPosition());
            else onMoveCameraWithMe(mMarker.getPosition(), car);
        }
    }


    private void onMoveCameraWithMe(final LatLng position, final Car car) {

        LatLng target = new LatLng(car.getLat(), car.getLon());

        LatLngBounds bounds = LatLngBounds
                .builder()
                .include(position)
                .include(target)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getPx(20)));
    }

    private void onMoveCamera(final Car car) {

        LatLng target = new LatLng(car.getLat(), car.getLon());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 15f);
        mMap.animateCamera(cameraUpdate, 1500, null);

    }

    private int getPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onForbidden() {
        mProfileInteractor.logout(this);
    }

    @Override
    public void onTariffNotFound() {
        Timber.d("onTariffNotFound");
        //TODO Что делать при этой ошибке CarInteractorImpl
    }

    @Override
    public void onBook(Car car) {
        Timber.tag("Action").d("onBook " + car.toString());

        isInfoPopup = false;

        if (mDialog.isShowing()) {
            bInfo.btnBook.setProgress(100);
            mDialog.dismiss();
        }

        mCar = car;

        if (mCar.isInTransfer()){
            if (bCloseCar != null){
                bCloseCar.btnCloseRent.setVisibility(View.GONE);
            }
        }


        clear();
        if (mPolygons != null)
            for (PolygonOptions polygon : mPolygons)
                mMap.addPolygon(polygon);
        else mMapsInteractor.getInfo(this);
        onStatus(BOOKING);

        addMarker(car);
        startActivityForResult(new Intent(MapsActivity.this, BookCarActivity.class).putExtra("car", mCar), RC_BOOK);
    }

    @Override
    public void onCar(Car car) {
        mCar = car;

        clear();
        if (mPolygons != null)
            for (PolygonOptions polygon : mPolygons)
                mMap.addPolygon(polygon);
        else mMapsInteractor.getInfo(this);
        addMarker(car);
        if (!isMoveCamera) {
            isMoveCamera = true;
            onMoveCamera(car);
        }
    }

    @Override
    public void onStatus(Status status) {
        mStatus = status;

        if (!USAGE.equals(mStatus)) {
            mMarker.setVisible(false);
            buildUserLocation(mLastLocation);
        }

        switch (status) {
            case BOOKING:
                if (!isInfoPopup) showDistancePopup(mCar.getWalktime());
                if (!isShowCommandPopup) showCommandPopup();
                mMap.setInfoWindowAdapter(new CustomWindowAdapter());
                mMap.getUiSettings().setMapToolbarEnabled(false);
                break;
            case PARKING:
            case USAGE:
                hideTopWindow();
                showClosePopup();
                mMap.setInfoWindowAdapter(new CustomWindowAdapter());
                break;
            default:
                mMap.setInfoWindowAdapter(null);
                mMap.setOnMarkerClickListener(marker -> {
                    showCarsDialog(mMarkerCar.get(marker));
                    return false;
                });
                mMap.getUiSettings().setMapToolbarEnabled(true);
        }

    }

    private boolean isProcessing() {
        if (isShowClosePopup && bCloseCar != null) {
            if (bCloseCar.btnCloseOrOpen != null && bCloseCar.btnCloseOrOpen.getProgress() == 50) {
                return true;
            }
            if (bCloseCar.btnCloseRent != null && bCloseCar.btnCloseRent.getProgress() == 50) {
                return true;
            }

        }

        if (isShowCommandPopup && bOpenCar != null) {
            if (bOpenCar.btnOpen != null && bOpenCar.btnOpen.getProgress() == 50) {
                return true;
            }
            if (bOpenCar.btnCancel != null && bOpenCar.btnCancel.getProgress() == 50) {
                return true;
            }
        }

        return false;

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


    private void drawPolygon(PolygonResponse coordList) {
        if (mPolygons == null) mPolygons = new ArrayList<>();
        mPolygons.clear();
        for (List<Coord> coords : coordList.getArea()) {

            PolygonOptions polygon = new PolygonOptions()
                    .fillColor(getResources().getColor(R.color.polygonColor))
                    .strokeColor(getResources().getColor(android.R.color.transparent))
                    .geodesic(true);
            for (Coord coord : coords) {
                polygon.add(coord.toLatLng());
            }
            mPolygons.add(polygon);
        }

        for (PolygonOptions _polygon : mPolygons)
            mMap.addPolygon(_polygon);

    }

    @Override
    public void onCheck(Check check) {
        onBookingTimeLeft(check.getBookingTimeLeft());
        mCheck = check;
        if (isShowClosePopup) {
            showClosePopup();
        }
    }

    @Override
    public void onUnknownError(String text) {
        unlock(text);
    }

    @Override
    public void onCarNotFound(String text) {
        if (bInfo.btnBook != null) {
            AppUtils.error(text, bInfo.btnBook);
        }
    }

    @Override
    public void onNotInfo(String text) {
        if (bInfo.btnBook != null) {
            AppUtils.error(text, bInfo.btnBook);
        }
    }

    @Override
    public void onNotOrder(String text) {
        if (bInfo.btnBook != null) {
            AppUtils.error(text, bInfo.btnBook);
        }
    }

    @Override
    public void onToken(Command command, String token) {
        mToken = token;
        mCommand = command;
    }

    @Override
    public void onPleaseWait() {
        if (mToken != null && mCommand != null) {

            new Handler().postDelayed(() -> mCarInteractor.result(mCommand, mToken, MapsActivity.this), 3000);
        }
    }

    @Override
    public void onCommandError() {
        unlock(getString(R.string.command_error));
    }

    @Override
    public void onOpen() {
        mMapsInteractor.getStatusCar(this);/*onStatus(Status.USAGE);*/
        onStatus(USAGE);
        if (bOpenCar != null && bOpenCar.btnOpen != null) {
            AppUtils.success(bOpenCar.btnOpen);
        }

        if (bCloseCar != null && bCloseCar.btnCloseOrOpen != null) {
            bCloseCar.btnCloseRent.setEnabled(true);
            AppUtils.success(bCloseCar.btnCloseOrOpen, getString(R.string.close_car));
        }

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
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationWithCheckPermissions();
        if (mLastLocation != null) {
            updateLocation(mLastLocation);
            if (mMarkerCar.isEmpty()) {
                mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
            }

            if (mPolygons == null) mMapsInteractor.getInfo(this);

        } else {
            mLastLocation = new Location("");
            mLastLocation.setLatitude(55.749792);
            mLastLocation.setLongitude(37.632495);// Create moscow coordinates;
            animateCamera(new LatLng(55.749792, 37.632495));
            isFake = true;
            updateLocation(mLastLocation);

            if (mMarkerCar.isEmpty()) {
                mMapsInteractor.getStatusCars(0, 0, MapsActivity.this);
            }
            if (mPolygons == null) mMapsInteractor.getInfo(this);

            showToast("Не удалось определить месторасположение");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Timber.e("Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        animateCamera(new LatLng(55.749792, 37.632495));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateLocation(location);
    }

    private void addBottomWindow(View view) {
        /*if (mCar == null || !mCar.isTransferable()) {
            Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);
            b.ltContainer.setAnimation(anim);
        }*/
        b.ltContainer.addView(view);
    }

    private void hideBottomWindow() {
        isShowCommandPopup = false;
        isShowClosePopup = false;
        b.ltContainer.removeAllViews();
    }

    private void addTopWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.top_down);

        b.ltInfo.setAnimation(anim);
        b.ltInfo.addView(view);
    }

    private void hideTopWindow() {
        isInfoPopup = false;
        b.ltInfo.removeAllViews();
    }

    private void showDistancePopup(int walktime) {
        hideTopWindow();

        isInfoPopup = true;
        PopupDistance popupDistance = DataBindingUtil.inflate(getLayoutInflater(), R.layout.popup_distance, null, false);
        addTopWindow(popupDistance.getRoot());

        if (NORMAL.equals(mStatus)) {
            popupDistance.txtDistance.setText(getString(R.string.distance_to_car, AppUtils.toTime(walktime)));
            hideBottomWindow();
        } else {
            popupDistance.txtDistance.setText(getString(R.string.distance_to_book_car, AppUtils.toTime(walktime)));
        }

    }


    private void showCommandPopup() {
        hideBottomWindow();

        isShowCommandPopup = true;
        bOpenCar = DataBindingUtil.inflate(getLayoutInflater(), R.layout.popup_open_car, null, false);
        bOpenCar.btnNavigate.setProgress(100);
        addBottomWindow(bOpenCar.getRoot());
        bOpenCar.setListener(this);
        bOpenCar.btnCancel.setVisibility(mCar.isInTransfer() ? View.GONE : View.VISIBLE);
        bOpenCar.btnCancel.setText(getString(mCar.isTransferable() ? R.string.transfer_car : R.string.cancel_reserve));
        bOpenCar.btnOpen.setIndeterminateProgressMode(true);
        bOpenCar.btnCancel.setIndeterminateProgressMode(true);
    }


    public void onButtonOpen(View view) {
        mMapsInteractor.clearSubscriptions();
        bOpenCar.btnOpen.setProgress(50);
        bOpenCar.btnCancel.setEnabled(false);

        mCarInteractor.command(Command.OPEN, MapsActivity.this);
    }

    public void onButtonCancel(View view) {
        if (mCar.isTransferable()) {
            new MaterialDialog
                    .Builder(MapsActivity.this)
                    .title(R.string.transfer_finish_title)
                    .content(R.string.transfer_finish_info)
                    .widgetColorRes(R.color.white)
                    .positiveText(R.string.ok_action)
                    .negativeText(R.string.cancel_action)
                    .onPositive((dialog, which) -> {
                        mMapsInteractor.clearSubscriptions();
                        bOpenCar.btnCancel.setEnabled(false);
                        bOpenCar.btnCancel.setProgress(50);
                        mCarInteractor.command(Command.TRANSFER, MapsActivity.this);
                    })
                    .onNegative((dialog, which) -> {

                    })
                    .positiveColor(getResources().getColor(R.color.main_dark))
                    .negativeColor(getResources().getColor(R.color.main_dark))
                    .autoDismiss(true)
                    .show();
        } else {
            mMapsInteractor.clearSubscriptions();
            bOpenCar.btnCancel.setProgress(50);
            mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
            bOpenCar.btnOpen.setEnabled(false);
        }
    }

    public void onButtonNavigate(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mCar.getLat() + ", " + mCar.getLon() + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            final String appPackageName = "com.google.android.apps.maps";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
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

    public void onBookClicked(View view) {
        mDialog.getBuilder().autoDismiss(false);
        if (view.getTag() != null && mLastLocation.getLatitude() > 0.0d && mLastLocation.getLongitude() > 0.0d) {

            if (mCar.isTransferable()) {
                new MaterialDialog
                        .Builder(MapsActivity.this)
                        .title(R.string.transfer_book_title)
                        .content(R.string.transfer_book_info)
                        .widgetColorRes(R.color.white)
                        .positiveText(R.string.ok_action)
                        .negativeText(R.string.cancel_action)
                        .onPositive((dialog, which) -> {
                            mMapsInteractor.clearSubscriptions();
                            bInfo.btnBook.setProgress(50);
                            mCarInteractor.booking((String) view.getTag(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);

                        })
                        .onNegative((dialog, which) -> {
                            mDialog.dismiss();
                        })
                        .positiveColor(getResources().getColor(R.color.main_dark))
                        .negativeColor(getResources().getColor(R.color.main_dark))
                        .autoDismiss(true)
                        .show();

            } else {
                mMapsInteractor.clearSubscriptions();
                bInfo.btnBook.setProgress(50);
                mCarInteractor.booking((String) view.getTag(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
            }


        } else {

            showToast("Не удалось установить месторасположение");
        }
    }


    private void showClosePopup() {
        hideBottomWindow();

        isShowClosePopup = true;
        bCloseCar = DataBindingUtil.inflate(getLayoutInflater(), R.layout.popup_close_car, null, false);
        addBottomWindow(bCloseCar.getRoot());
        bCloseCar.setListener(this);

        bCloseCar.btnCloseRent.setVisibility(mCar.isInTransfer() ? View.GONE : View.VISIBLE);
        bCloseCar.btnCloseRent.setText(mCar.isTransferable() && !mCar.isInTransfer()  ? getString(R.string.transfer_car) : getString(R.string.close_rent));
        bCloseCar.btnCloseRent.setIdleText(mCar.isTransferable() && !mCar.isInTransfer() ? getString(R.string.transfer_car) : getString(R.string.close_rent));

        bCloseCar.btnCloseOrOpen.setIndeterminateProgressMode(true);
        bCloseCar.btnCloseRent.setIndeterminateProgressMode(true);
        if (PARKING.equals(mStatus) || BOOKING.equals(mStatus)) {
            bCloseCar.btnNavigate.setVisibility(View.VISIBLE);
            bCloseCar.btnCloseOrOpen.setText(getString(R.string.open_car));
            bCloseCar.btnCloseOrOpen.setIdleText(getString(R.string.open_car));
            bCloseCar.btnNavigate.setProgress(100);
        } else {
            bCloseCar.btnNavigate.setVisibility(View.GONE);
            bCloseCar.btnCloseOrOpen.setText(getString(R.string.close_car));
            bCloseCar.btnCloseOrOpen.setIdleText(getString(R.string.close_car));
        }


        if (PARKING.equals(mStatus)) {
            bCloseCar.txtTariff.setText("Парковка");
            bCloseCar.txtPerMin.setText(convertRub(mCar.getTariff().getParking()));
        } else if (USAGE.equals(mStatus)) {
            bCloseCar.txtTariff.setText("Использование");
            bCloseCar.txtPerMin.setText(convertRub(mCar.getTariff().getUsage()));
        }

        if (mCheck == null) return;

        bCloseCar.txtTotalUsage.setText(convertRub(mCheck.getUsageWeekendCost() + mCheck.getUsageWorkdayCost()));
        bCloseCar.txtParking.setText(convertRub(mCheck.getParkingCost()));
        bCloseCar.txtTotal.setText(convertRub(mCheck.getParkingCost() + mCheck.getUsageWorkdayCost() + mCheck.getUsageWeekendCost()));
    }

    public void onCloseRent(View view) {
        if (mCar.isTransferable()) {
            new MaterialDialog
                    .Builder(MapsActivity.this)
                    .title(R.string.transfer_finish_title)
                    .content(R.string.transfer_finish_info)
                    .widgetColorRes(R.color.white)
                    .positiveText(R.string.ok_action)
                    .negativeText(R.string.cancel_action)
                    .onPositive((dialog, which) -> {
                        mMapsInteractor.clearSubscriptions();
                        bCloseCar.btnCloseOrOpen.setEnabled(false);
                        bCloseCar.btnCloseRent.setProgress(50);
                        mCarInteractor.command(Command.TRANSFER, MapsActivity.this);
                    })
                    .onNegative((dialog, which) -> {

                    })
                    .positiveColor(getResources().getColor(R.color.main_dark))
                    .negativeColor(getResources().getColor(R.color.main_dark))
                    .autoDismiss(true)
                    .show();
        } else {
            mMapsInteractor.clearSubscriptions();
            bCloseCar.btnCloseOrOpen.setEnabled(false);
            bCloseCar.btnCloseRent.setProgress(50);
            mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
        }

    }

    public void onCloseOrOpen(View view) {
        mMapsInteractor.clearSubscriptions();
        bCloseCar.btnCloseRent.setEnabled(false);
        bCloseCar.btnCloseOrOpen.setProgress(50);

        if (!USAGE.equals(mStatus)) mCarInteractor.command(Command.OPEN, MapsActivity.this);
        else mCarInteractor.command(Command.CLOSE, MapsActivity.this);

    }



    public String convertRub(long kopeck) {
        return String.format("%.2f", (float) kopeck / 100) + " руб.";
    }

    //used by layout
    public String convertRubPerMin(long kopeck) {
        return String.format("%.2f", (float) kopeck / 100) + " руб./мин.";
    }

    @Override
    public void onPolygonSuccess(PolygonResponse response) {
        drawPolygon(response);
    }

    @Override
    public void onPolygonFailed() {
        if (mPolygons == null) mMapsInteractor.getInfo(this); //try again

    }

    private class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (marker.equals(mMarker)) {
                return null;
            }
            MarkerInfo bMarkerInfo = DataBindingUtil.inflate(getLayoutInflater(), R.layout.marker_info, null, false);
            bMarkerInfo.rlDiscount.setVisibility(mCar.getDiscount()>0 ? View.VISIBLE : View.GONE);
            bMarkerInfo.tvDiscount.setText("-"+mCar.getDiscount()+"%");
            bMarkerInfo.txtColor.setText(mCar.getColor());
            bMarkerInfo.txtModel.setText(mCar.getModel());
            bMarkerInfo.txtNumber.setText(mCar.getNumber());
            if (BOOKING.equals(mStatus)) {
                bMarkerInfo.txtStartUsage.setVisibility(View.VISIBLE);
                String arendaBegining = "До начала аренды " + mBookingTimeLeft / 60 + "мин";
                bMarkerInfo.txtStartUsage.setText(arendaBegining);
            }

            return bMarkerInfo.getRoot();
        }
    }

    @Override
    protected void onDestroy() {
        mMapsInteractor.getSubscription().unsubscribe();
        mProfileInteractor.getSubscription().unsubscribe();
        mCarInteractor.getSubscription().unsubscribe();
        super.onDestroy();
    }


    private void call() {
        checkPhonePermission(() -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+74993223875"))));
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_BOOK) {
            onMoveCamera(mCar);
        } else if (requestCode == RC_CHECK) {
            mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), this);
        }
    }
}
