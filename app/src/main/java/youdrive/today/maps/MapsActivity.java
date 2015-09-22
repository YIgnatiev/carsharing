package youdrive.today.maps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dd.CircularProgressButton;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.AppUtils;
import youdrive.today.BaseActivity;
import youdrive.today.Car;
import youdrive.today.Check;
import youdrive.today.Command;
import youdrive.today.Menu;
import youdrive.today.R;
import youdrive.today.Status;
import youdrive.today.User;
import youdrive.today.car.CarActionListener;
import youdrive.today.car.CarInteractorImpl;
import youdrive.today.data.PreferenceHelper;
import youdrive.today.login.activities.LoginActivity;
import youdrive.today.other.BookCarActivity;
import youdrive.today.other.CompleteActivity;
import youdrive.today.profile.ProfileActionListener;
import youdrive.today.profile.ProfileAdapter;
import youdrive.today.profile.ProfileInteractorImpl;
import youdrive.today.response.Coord;

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener, CarActionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener ,PolygonListener{

    private static final int RC_BOOK = 0;
    private static final int RC_CHECK = 1;

    private GoogleMap mMap;
    private PolygonOptions mPolygon;
    private ProfileInteractorImpl mProfileInteractor;

    @InjectView(R.id.drawer)
    DrawerLayout mDrawer;

    @InjectView(R.id.lvProfile)
    ListView lvProfile;

    @InjectView(R.id.ltContainer)
    FrameLayout ltContainer;

    @InjectView(R.id.ltInfo)
    FrameLayout ltInfo;


    private String mToken;
    private CarInteractorImpl mCarInteractor;
    private Car mCar;
    private Command mCommand;
    private MapsInteractorImpl mMapsInteractor;
    private GoogleApiClient mGoogleApiClient;
    private Marker mMarker;
    private LocationRequest mLocationRequest;
    private float mZoomLevel;
    private Check mCheck;

    private Location mLastLocation;
    private int mBookingTimeLeft;
    private MaterialDialog mDialog;
    private Status mStatus;

    private CircularProgressButton btnBook;
    private CircularProgressButton btnOpen;
    private CircularProgressButton btnCancel;
    private CircularProgressButton btnCloseOrOpen;
    private CircularProgressButton btnCloseRent;

    private View mView;
    private User mUser;

    private boolean isShowCommandPopup = false;
    private boolean isShowClosePopup = false;
    private boolean isInfoPopup = false;
    private boolean isMoveCamera = false;
    private boolean isMoveCameraWithMe = false;

    private boolean isFake = false;
    private Timer mTimer;

    @OnItemClick(R.id.lvProfile)
    void onItemSelected(int position) {
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
                mProfileInteractor.logout(this);
                break;
        }
    }

    private void call() {
        startActivity(new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:+74993223875")));
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)));
    }

    @OnClick(R.id.btnZoomIn)
    void onZoomIn(View v) {
        if (mMap != null) {
            mZoomLevel = mMap.getCameraPosition().zoom;
            if (mZoomLevel < mMap.getMaxZoomLevel()) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(++mZoomLevel));
            }
        }
    }

    @OnClick(R.id.btnZoomOut)
    void onZoomOut(View v) {
        if (mMap != null) {
            mZoomLevel = mMap.getCameraPosition().zoom;
            if (mZoomLevel > mMap.getMinZoomLevel()) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(--mZoomLevel));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);
        setActionBarIcon(R.drawable.ic_ab_drawer);
        setUpMapIfNeeded();


        createLocationRequest();

        mZoomLevel = mMap.getMinZoomLevel();

        if (App.getInstance().getPreference() != null) {
            if (App.getInstance().getPreference().getUser() != null) {
                mUser = new Gson().fromJson(App.getInstance().getPreference().getUser(), User.class);
            }
        }

        View header = getLayoutInflater().inflate(R.layout.header_profile, null);
        TextView txtName = ButterKnife.findById(header, R.id.txtName);

        if (mUser != null) {
            txtName.setText(mUser.getName());
        }

        lvProfile.addHeaderView(header);
        lvProfile.setAdapter(new ProfileAdapter(this, R.layout.item_profile, getMenu()));

        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        mProfileInteractor = new ProfileInteractorImpl();
        mCarInteractor = new CarInteractorImpl();
        mMapsInteractor = new MapsInteractorImpl();

        buildGoogleApiClient();
        checkInternet();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        mTimer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mStatus != null && mLastLocation != null) {
                    if (Status.NORMAL.equals(mStatus) || Status.BOOKING.equals(mStatus)) {
                        mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
                    } else {
                        mMapsInteractor.getStatusCar(MapsActivity.this);
                    }
                }
            }
        }, 20 * 1000, 20 * 1000);
    }


    private void checkInternet() {
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_LONG).show();
            animateCamera(new LatLng(55.749792, 37.632495));
        }
    }

    private void animateCamera(LatLng position) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 11);
        mMap.animateCamera(cameraUpdate);
    }


    protected synchronized void buildGoogleApiClient() {
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
    protected int getLayoutResource() {
        return R.layout.activity_maps;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.START);
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

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
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
        if (car == null) {
            return;
        }

        if (!Status.NORMAL.equals(mStatus)) {
            return;
        }

        mDialog = new MaterialDialog.Builder(MapsActivity.this)
                .customView(R.layout.dialog_info_contents, true)
                .widgetColorRes(R.color.white)
                .show();

        View view = mDialog.getCustomView();
        if (view != null) {
            buildDialog(view, car);
        }
    }

    private void buildDialog(View view, final Car car) {
        if (car.getModel() != null) {
            ((TextView) ButterKnife.findById(view, R.id.txtModel))
                    .setText(car.getModel());
        }

        ((TextView) ButterKnife.findById(view, R.id.txtDistance))
                .setText(AppUtils.toKm(car.getDistance()) + getString(R.string.km));
        ((TextView) ButterKnife.findById(view, R.id.txtTimeTo))
                .setText(AppUtils.toTime(car.getWalktime()) + getString(R.string.minutes));
        ((TextView) ButterKnife.findById(view, R.id.txtType))
                .setText(car.getTransmission());
        TextView txtFuel = ButterKnife.findById(view, R.id.txtFuel);
        if (car.getFuel() != null) {
            txtFuel.setText(String.valueOf(car.getFuel()));
        } else {
            txtFuel.setText("Неизвестно");
        }

        ((TextView) ButterKnife.findById(view, R.id.txtTaxDrive))
                .setText(convertRubPerMin(car.getTariff().getUsage()));
        ((TextView) ButterKnife.findById(view, R.id.txtTaxPark))
                .setText(convertRubPerMin(car.getTariff().getParking()));

        btnBook = buildButton(view, R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.getBuilder().autoDismiss(false);
                btnBook.setProgress(50);

                if (car.getId() != null && mLastLocation.getLatitude() > 0.0d && mLastLocation.getLongitude() > 0.0d) {

                    mCarInteractor.booking(car.getId(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);

                } else {

                    Toast.makeText(MapsActivity.this, "Не удалось установить месторасположение", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                        MarkerOptions markerOptions = new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(car.getLat(), car.getLon()))
                                .title(car.getModel())
                                .icon(BitmapDescriptorFactory.fromBitmap(resource));
                        mMarkerCar.put(mMap.addMarker(markerOptions), car);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(car.getLat(), car.getLon()))
                                .title(car.getModel())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location));
                        mMarkerCar.put(mMap.addMarker(markerOptions), car);
                    }
                });


//        Picasso.with(this)
//                .load(car.getPointer_resource()+"_android.png")
//                .resize(80, 100)
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//
//
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
//
//               // .into(new PicassoMarker(markerOptions));


    }

    @Override
    public void onLogout() {
        Timber.tag("Action").d("Logout");
        startActivity(new Intent(this, LoginActivity.class));
        new PreferenceHelper(this).clear();
    }

    @Override
    public void onError() {

        Timber.tag("Error").d("Internal Error");
        String text = getString(R.string.internal_error);
        if (btnCancel != null
                && btnCancel.getProgress() == 50) {
            AppUtils.error(text, btnCancel);
            btnOpen.setEnabled(true);
        } else if (btnCloseRent != null
                && btnCloseRent.getProgress() == 50) {
            AppUtils.error(text, btnCloseRent);
            btnCloseOrOpen.setEnabled(true);
        } else if (btnOpen != null
                && btnOpen.getProgress() == 50) {
            AppUtils.error(text, btnOpen);
            btnCancel.setEnabled(true);
        } else if (btnCloseOrOpen != null
                && btnCloseOrOpen.getProgress() == 50) {
            AppUtils.error(text, btnCloseOrOpen);
            btnCloseRent.setEnabled(true);
        } else if (btnBook != null
                && btnBook.getProgress() == 50) {
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onAccessDenied(String text) {
        Timber.tag("Error").e("onAccessDenied");
        unlock(text);
    }

    private void unlock(String text) {
        if (btnCancel != null && btnCancel.getProgress() == 50) {
            AppUtils.error(text, btnCancel);
            btnOpen.setEnabled(true);

        } else if (btnCloseRent != null && btnCloseRent.getProgress() == 50) {
            AppUtils.error(text, btnCloseRent);
            btnCloseOrOpen.setEnabled(true);

        } else if (btnOpen != null && btnOpen.getProgress() == 50) {
            AppUtils.error(text, btnOpen);
            btnCancel.setEnabled(true);

        } else if (btnCloseOrOpen != null && btnCloseOrOpen.getProgress() == 50) {
            AppUtils.error(text, btnCloseOrOpen);
            btnCloseRent.setEnabled(true);
        }
    }

    @Override
    public void onCommandNotSupported(String text) {
        Timber.tag("Error").e("onCommandNotSupported");
        unlock(text);
    }

    @Override
    public void onClose() {
        Timber.tag("Action").d("onClose");
        onStatus(Status.PARKING);
        if (btnCloseOrOpen != null) {
            btnCloseRent.setEnabled(true);
            AppUtils.success(btnCloseOrOpen, getString(R.string.open_car));
        }

        mMapsInteractor.getStatusCar(this);
    }

    @Override
    public void onComplete(Check check) {
        Timber.tag("Action").d("onComplete " + check.toString());
        hideBottomWindow();
        startActivityForResult(new Intent(this, CompleteActivity.class).putExtra("check", check), RC_CHECK);
        mMapsInteractor.getStatusCar(this);
    }

    @Override
    public void onBookingTimeLeft(int bookingTimeLeft) {
        Timber.tag("Action").d("onBookingTimeLeft " + bookingTimeLeft);
        mBookingTimeLeft = bookingTimeLeft;

//        if (Status.BOOKING.equals(mStatus)
//                && !isInfoPopup){
//            showInfoPopup(bookingTimeLeft);
//        }

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

        if(mPolygon != null)mMap.addPolygon(mPolygon);
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (car.getLat() > position.latitude) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            new LatLngBounds(
                                    position,
                                    new LatLng(car.getLat(), car.getLon())),
                            getPx(20)));
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            new LatLngBounds(
                                    new LatLng(car.getLat(), car.getLon()),
                                    position),
                            getPx(20)));
                }
            }
        });
    }

    private void onMoveCamera(final Car car) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(car.getLat(), car.getLon()),
                15f), 1500, null);
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
            btnBook.setProgress(100);
            mDialog.dismiss();
        }

        mCar = car;

        clear();
        if(mPolygon != null)mMap.addPolygon(mPolygon);
        else mMapsInteractor.getInfo(this);
        onStatus(Status.BOOKING);

        addMarker(car);
        startActivityForResult(new Intent(MapsActivity.this, BookCarActivity.class).putExtra("car", mCar), RC_BOOK);
    }

    @Override
    public void onCar(Car car) {
        mCar = car;

        clear();
        if(mPolygon != null)mMap.addPolygon(mPolygon);
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

        if (!Status.USAGE.equals(mStatus)) {
            mMarker.setVisible(false);
            buildUserLocation(mLastLocation);
        }

        if (Status.BOOKING.equals(status)) {
            if (!isInfoPopup) {
                showDistancePopup(mCar.getWalktime());
            }
        } else if (Status.PARKING.equals(status) || Status.USAGE.equals(status)) {
            hideTopWindow();
        }

        if (Status.BOOKING.equals(status)) {
            if (!isShowCommandPopup) {
                showCommandPopup();
            }
        } else if (Status.PARKING.equals(status)
                || Status.USAGE.equals(status)) {
            if (!isShowClosePopup) {
                showClosePopup();
            }
        }

        if (Status.BOOKING.equals(mStatus)
                || Status.USAGE.equals(mStatus)
                || Status.PARKING.equals(mStatus)) {
            mMap.setInfoWindowAdapter(new CustomWindowAdapter());
        } else {
            mMap.setInfoWindowAdapter(null);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    showCarsDialog(mMarkerCar.get(marker));
                    return false;
                }
            });
        }
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



    private void drawLine(List<Coord> coordList) {

            mPolygon = new PolygonOptions()
                    .fillColor(getResources().getColor(R.color.polygonColor))
                    .strokeColor(getResources().getColor(android.R.color.transparent))
                    .geodesic(true);

            for(Coord coord :coordList)
                mPolygon.add(coord.toLatLng());
            mPolygon.add(coordList.get(0).toLatLng());
             mMap.addPolygon(mPolygon);


    }

    @Override
    public void onCheck(Check check) {
        onBookingTimeLeft(check.getBookingTimeLeft());
        mCheck = check;
        updateCheck(mView);
    }

    @Override
    public void onUnknownError(String text) {
        unlock(text);
    }

    @Override
    public void onCarNotFound(String text) {
        if (btnBook != null) {
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onNotInfo(String text) {
        if (btnBook != null) {
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onNotOrder(String text) {
        if (btnBook != null) {
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onToken(Command command, String token) {
        mToken = token;
        mCommand = command;
    }

    @Override
    public void onPleaseWait() {
        if (mToken != null
                && mCommand != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCarInteractor.result(mCommand, mToken, MapsActivity.this);
                }
            }, 3000);
        }
    }

    @Override
    public void onCommandError() {
        unlock(getString(R.string.command_error));
    }

    @Override
    public void onOpen() {
        if (btnOpen != null) {
            AppUtils.success(btnOpen);
        }

        if (btnCloseOrOpen != null) {
            btnCloseRent.setEnabled(true);
            AppUtils.success(btnCloseOrOpen, getString(R.string.close_car));
        }

        mMapsInteractor.getStatusCar(this);/*onStatus(Status.USAGE);*/
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
        startLocationUpdates();
        if (mLastLocation != null) {
            updateLocation(mLastLocation);
            if (mMarkerCar.isEmpty()) {
                mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
            }

            if(mPolygon == null) mMapsInteractor.getInfo(this);

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
            if(mPolygon == null) mMapsInteractor.getInfo(this);

            Toast.makeText(this, "Не удалось определить месторасположение", Toast.LENGTH_LONG).show();
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
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);

        ltContainer.setAnimation(anim);
        ltContainer.addView(view);
    }

    private void hideBottomWindow() {
        isShowCommandPopup = false;
        isShowClosePopup = false;
        ltContainer.removeAllViews();
    }

    private void addTopWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.top_down);

        ltInfo.setAnimation(anim);
        ltInfo.addView(view);
    }

    private void hideTopWindow() {
        isInfoPopup = false;
        ltInfo.removeAllViews();
    }

    private void showDistancePopup(int walktime) {
        hideTopWindow();

        isInfoPopup = true;

        View view = getLayoutInflater().inflate(R.layout.popup_distance, null, false);
        addTopWindow(view);

        TextView txtDistance = ButterKnife.findById(view, R.id.txtDistance);
        if (Status.NORMAL.equals(mStatus)) {
            txtDistance.setText(getString(R.string.distance_to_car, AppUtils.toTime(walktime)));
        } else {
            txtDistance.setText(getString(R.string.distance_to_book_car, AppUtils.toTime(walktime)));
        }

    }

    private void showInfoPopup(int bookingTimeLeft) {
        hideTopWindow();

        isInfoPopup = true;

        View view = getLayoutInflater().inflate(R.layout.popup_start_rent, null, false);
        addTopWindow(view);

        TextView txtStartRent = ButterKnife.findById(view, R.id.txtStartRent);
        txtStartRent.setText(getString(R.string.time_start_rent, AppUtils.toTime(bookingTimeLeft)));
    }

    private void showCommandPopup() {
        hideBottomWindow();

        isShowCommandPopup = true;

        View view = getLayoutInflater().inflate(R.layout.popup_open_car, null, false);
        addBottomWindow(view);

        btnOpen = buildButton(view, R.id.btnOpen);
        btnCancel = buildButton(view, R.id.btnCancel);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOpen.setProgress(50);
                btnCancel.setEnabled(false);

                mCarInteractor.command(Command.OPEN, MapsActivity.this);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel.setProgress(50);
                mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
                btnOpen.setEnabled(false);
            }
        });
    }

    private CircularProgressButton buildButton(View view, int id) {
        CircularProgressButton btn = ButterKnife.findById(view, id);
        btn.setIndeterminateProgressMode(true);
        return btn;
    }

    private void showClosePopup() {
        hideBottomWindow();

        isShowClosePopup = true;

        mView = getLayoutInflater().inflate(R.layout.popup_close_car, null, false);
        addBottomWindow(mView);

        btnCloseOrOpen = buildButton(mView, R.id.btnCloseOrOpen);
        btnCloseRent = buildButton(mView, R.id.btnCloseRent);

        if (Status.PARKING.equals(mStatus)) {
            btnCloseOrOpen.setText(getString(R.string.open_car));
            btnCloseOrOpen.setIdleText(getString(R.string.open_car));
        } else {
            btnCloseOrOpen.setText(getString(R.string.close_car));
            btnCloseOrOpen.setIdleText(getString(R.string.open_car));
        }

        btnCloseOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCloseRent.setEnabled(false);
                btnCloseOrOpen.setProgress(50);
                if (Status.PARKING.equals(mStatus)) {
                    mCarInteractor.command(Command.OPEN, MapsActivity.this);
                } else {
                    mCarInteractor.command(Command.CLOSE, MapsActivity.this);
                }
            }
        });

        btnCloseRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCloseOrOpen.setEnabled(false);
                btnCloseRent.setProgress(50);
                mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
            }
        });

        if (mCheck != null) {
            updateCheck(mView);
        }
    }

    private void updateCheck(View view) {
        if (view != null) {
            TextView txtTariff = ButterKnife.findById(view, R.id.txtTariff);
            TextView txtPerMin = ButterKnife.findById(view, R.id.txtPerMin);

            if (Status.PARKING.equals(mStatus)) {
                txtTariff.setText("Парковка");
                txtPerMin.setText(convertRub(mCar.getTariff().getParking()));
            } else if (Status.USAGE.equals(mStatus)) {
                txtTariff.setText("Использование");
                txtPerMin.setText(convertRub(mCar.getTariff().getUsage()));
            }

            ((TextView) ButterKnife.findById(view, R.id.txtTotalUsage))
                    .setText(convertRub(mCheck.getUsageWeekendCost()
                            + mCheck.getUsageWorkdayCost()));
            ((TextView) ButterKnife.findById(view, R.id.txtParking))
                    .setText(convertRub(mCheck.getParkingCost()));
            ((TextView) ButterKnife.findById(view, R.id.txtTotal))
                    .setText(convertRub(mCheck.getParkingCost()
                            + mCheck.getUsageWorkdayCost()
                            + mCheck.getUsageWeekendCost()));
        }
    }

    private String convertRub(long kopeck) {
        return String.format("%.2f", (float) kopeck / 100) + " руб.";
    }

    private String convertRubPerMin(long kopeck) {
        return String.format("%.2f", (float) kopeck / 100) + " руб./мин.";
    }

    @Override
    public void onPolygonSuccess(List<Coord> coordList) {
        drawLine(coordList);
    }

    @Override
    public void onPolygonFailed() {
        if(mPolygon == null) mMapsInteractor.getInfo(this); //try again

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
            View view = getLayoutInflater().inflate(R.layout.marker_info, null);

            ((TextView) ButterKnife.findById(view, R.id.txtModel))
                    .setText(mCar.getModel());
            ((TextView) ButterKnife.findById(view, R.id.txtNumber))
                    .setText(mCar.getNumber());
            ((TextView) ButterKnife.findById(view, R.id.txtColor))
                    .setText(mCar.getColor());

            if (Status.BOOKING.equals(mStatus)) {

                LinearLayout ltRentInfo = ButterKnife.findById(view, R.id.ltRentInfo);
                ltRentInfo.setVisibility(View.VISIBLE);

                ((TextView) ButterKnife.findById(view, R.id.txtStartUsage))
                        .setText("До начала аренды " + AppUtils.toTime(mBookingTimeLeft) + getString(R.string.minutes));
            }

            return view;
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return (activeInfo != null && activeInfo.isConnected());
    }


    @Override
    protected void onDestroy() {
        mMapsInteractor.getSubscription().unsubscribe();
        mProfileInteractor.getSubscription().unsubscribe();
        mCarInteractor.getSubscription().unsubscribe();
        super.onDestroy();
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
