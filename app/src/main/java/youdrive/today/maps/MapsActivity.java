package youdrive.today.maps;

import android.content.Intent;
import android.location.Location;
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
import com.dd.CircularProgressButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import youdrive.today.car.CarActionListener;
import youdrive.today.car.CarInteractorImpl;
import youdrive.today.other.CompleteActivity;
import youdrive.today.other.BookCarActivity;
import youdrive.today.profile.ProfileActionListener;
import youdrive.today.profile.ProfileAdapter;
import youdrive.today.profile.ProfileInteractorImpl;

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener, CarActionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int RC_BOOK = 0;
    private static final int RC_CHECK = 0;

    private GoogleMap mMap;

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
    private MarkerOptions mMarker;
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
    private boolean isShowClosePopup = false;

    private View mView;

    @OnItemClick(R.id.lvProfile)
    void onItemSelected(int position) {
        switch (position) {
            case 1:
                Timber.d("TARIFF");
                break;
            case 2:
                Timber.d("HELP");
                break;
            case 3:
                Timber.d("CALL");
                break;
            case 4:
                Timber.d("LOGOUT");
                mProfileInteractor.logout(MapsActivity.this);
                break;
        }
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

        lvProfile.addHeaderView(getLayoutInflater().inflate(R.layout.header_profile, null));
        lvProfile.setAdapter(new ProfileAdapter(this, R.layout.item_profile, getMenu()));
        
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        mProfileInteractor = new ProfileInteractorImpl();
        mCarInteractor = new CarInteractorImpl();
        mMapsInteractor = new MapsInteractorImpl();

        buildGoogleApiClient();
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
            if (mMap != null) {
                mMap.setOnMarkerClickListener(onMarkerClickListener);
            }
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
            mMarker = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Это Вы!")
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location));
            mMap.addMarker(mMarker);
        } else {
            mMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void showCarsDialog(final Car car) {
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
        ((TextView) ButterKnife.findById(view, R.id.txtModel))
                .setText(car.getModel());
        ((TextView) ButterKnife.findById(view, R.id.txtDistance))
                .setText(AppUtils.toKm(car.getDistance()) + getString(R.string.km));
        ((TextView) ButterKnife.findById(view, R.id.txtTimeTo))
                .setText(AppUtils.toTime(car.getWalktime()) + getString(R.string.minutes));
        ((TextView) ButterKnife.findById(view, R.id.txtType))
                .setText(car.getTransmission());
        ((TextView) ButterKnife.findById(view, R.id.txtTaxDrive))
                .setText(String.valueOf(car.getTariff().getUsage()));
        ((TextView) ButterKnife.findById(view, R.id.txtTaxPark))
                .setText(String.valueOf(car.getTariff().getParking()));

        btnBook = buildButton(view, R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog.getBuilder().autoDismiss(false);
                btnBook.setProgress(50);

                if (car.getId() != null
                        && mLastLocation.getLatitude() > 0.0d
                        && mLastLocation.getLongitude() > 0.0d) {
                    mCarInteractor.booking(car.getId(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
                } else {
                    Toast.makeText(MapsActivity.this, "Не удалось установить месторасположение", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {

            if (mMarkerCar.containsKey(marker)) {
                if (mMarkerCar.size() > 1) {
                    showCarsDialog(mMarkerCar.get(marker));
                } else {
                    mMap.setInfoWindowAdapter(new CustomWindowAdapter());
                }
            }

            return false;
        }
    };

    HashMap<Marker, Car> mMarkerCar = new HashMap<>();

    private void addMarker(Car car) {
        mMarkerCar.put(mMap.addMarker(
                        new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(car.getLat(), car.getLon()))
                                .title(car.getModel())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location))),
                car);
    }

    @Override
    public void onLogout() {
        Timber.d("LOGOUT");
    }

    @Override
    public void onError() {
        Timber.tag("Error").d("Internal Error");
        String text = getString(R.string.internal_error);
        if (btnCancel != null
                && btnCancel.getProgress() == 50){
            AppUtils.error(text, btnCancel);
        } else if (btnCloseRent != null
                && btnCloseRent.getProgress() == 50){
            AppUtils.error(text, btnCloseRent);
        } else if (btnOpen != null
                && btnOpen.getProgress() == 50){
            AppUtils.error(text, btnOpen);
        } else if (btnCloseOrOpen != null
                && btnCloseOrOpen.getProgress() == 50){
            AppUtils.error(text, btnCloseOrOpen);
        }
    }

    @Override
    public void onAccessDenied(String text) {
        Timber.tag("Error").e("onAccessDenied");
        errorCommand(text);
    }

    private void errorCommand(String text){
        if (btnCancel != null
                && btnCancel.getProgress() == 50){
            AppUtils.error(text, btnCancel);
        } else if (btnCloseRent != null
                && btnCloseRent.getProgress() == 50){
            AppUtils.error(text, btnCloseRent);
        } else if (btnOpen != null
                && btnOpen.getProgress() == 50){
            AppUtils.error(text, btnOpen);
        } else if (btnCloseOrOpen != null
                && btnCloseOrOpen.getProgress() == 50){
            AppUtils.error(text, btnCloseOrOpen);
        }
    }

    @Override
    public void onCommandNotSupported(String text) {
        Timber.tag("Error").e("onCommandNotSupported");
        errorCommand(text);
    }

    @Override
    public void onClose() {
        Timber.tag("Action").d("onClose");
        onStatus(Status.PARKING);
        if (btnCloseOrOpen != null) {
            btnCloseRent.setEnabled(true);
            AppUtils.success(btnCloseOrOpen, getString(R.string.open_car));
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
    public void onBookingTimeLeft(int bookingTimeLeft) {
        Timber.tag("Action").d("onBookingTimeLeft " + bookingTimeLeft);
        mBookingTimeLeft = bookingTimeLeft;
        showStartRentPopup(bookingTimeLeft);
    }

    @Override
    public void onSessionNotFound(String text) {
        Timber.tag("Error").e("onSessionNotFound");
        errorCommand(text);
    }

    @Override
    public void onCars(List<Car> cars) {
        Timber.tag("Action").d("onCars " + cars.toString());
        Collections.sort(cars);
        onMoveCameraWithMe(cars.get(0));
        for (Car c : cars) {
            addMarker(c);
        }

        showDistancePopup(cars.get(0).getWalktime());
    }

    private void onMoveCameraWithMe(final Car car) {
        if (mMarker != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            new LatLngBounds(
                                    new LatLng(car.getLat(), car.getLon()),
                                    mMarker.getPosition()),
                            getPx(20)));
                }
            });
        }
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
        Timber.d("onForbidden");
        //TODO Что делать при этой ошибке CarInteractorImpl
    }

    @Override
    public void onTariffNotFound() {
        Timber.d("onTariffNotFound");
        //TODO Что делать при этой ошибке CarInteractorImpl
    }

    @Override
    public void onBook(Car car) {
        Timber.tag("Action").d("onBook " + car.toString());

        if (mDialog.isShowing()) {
            btnBook.setProgress(100);
            mDialog.dismiss();
        }

        mCar = car;
        mMarkerCar.clear();
        mMap.clear();

        onStatus(Status.BOOKING);

        addMarker(car);
        startActivityForResult(new Intent(MapsActivity.this, BookCarActivity.class).putExtra("car", mCar), RC_BOOK);
    }

    @Override
    public void onCar(Car car) {
        Timber.tag("Action").d("OnCar " + car.toString());

        mCar = car;
        addMarker(car);
        onMoveCamera(car);
    }

    @Override
    public void onStatus(Status status) {
        Timber.tag("Action").d("onStatus " + status.toString());

        mStatus = status;

        if (Status.BOOKING.equals(status)) {
            showStartRentPopup(mBookingTimeLeft);
        } else if (Status.PARKING.equals(status)
                || Status.USAGE.equals(status)) {
            hideTopWindow();
        }

        if (Status.BOOKING.equals(status)) {
            showOpenPopup();
        } else if (!Status.NORMAL.equals(status) && !isShowClosePopup) {
            showClosePopup();
        }
    }

    @Override
    public void onCheck(Check check) {
        Timber.tag("Action").d("Check " + check.toString());
        onBookingTimeLeft(check.getBookingTimeLeft());
        mCheck = check;
        updateCheck(mView);
    }

    @Override
    public void onCarNotFound(String text) {
        Timber.tag("Error").e("onCarNotFound");
        if (btnBook != null){
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onNotInfo(String text) {
        Timber.tag("Error").e("onNotInfo");
        if (btnBook != null){
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onNotOrder(String text) {
        Timber.tag("Error").e("onNotOrder");
        if (btnBook != null){
            AppUtils.error(text, btnBook);
        }
    }

    @Override
    public void onToken(Command command, String token) {
        Timber.tag("onToken").d("Token " + token);
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
        //TODO Что делать при этой ошибке CarInteractorImpl
    }

    @Override
    public void onOpen() {
        Timber.d("onOpen");
        if (btnOpen != null) {
            AppUtils.success(btnOpen);
        }

        if (btnCloseOrOpen != null) {
            btnCloseRent.setEnabled(true);
            AppUtils.success(btnCloseOrOpen, getString(R.string.close_car));
        }

        onStatus(Status.USAGE);
    }

    @Override
    public void onSessionNotFound() {
        Timber.d("SessionNotFound");
        //TODO Что делать при этой ошибке
    }

    @Override
    public void onInvalidRequest() {
        Timber.d("InvalidRequest");
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
        } else {
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
        //TODO Что делать при этой ошибке
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    private void addBottomWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);

        ltContainer.setAnimation(anim);
        ltContainer.addView(view);
    }

    private void hideBottomWindow() {
        ltContainer.removeAllViews();
    }

    private void addTopWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.top_down);

        ltInfo.setAnimation(anim);
        ltInfo.addView(view);
    }

    private void hideTopWindow() {
        ltInfo.removeAllViews();
    }

    private void showDistancePopup(int walktime) {
        hideTopWindow();

        View view = getLayoutInflater().inflate(R.layout.popup_distance, null, false);
        addTopWindow(view);

        TextView txtDistance = ButterKnife.findById(view, R.id.txtDistance);
        txtDistance.setText("До ближайшей машины " + AppUtils.toTime(walktime) + getString(R.string.minutes) + " пешком");
    }

    private void showStartRentPopup(int bookingTimeLeft) {
        hideTopWindow();

        View view = getLayoutInflater().inflate(R.layout.popup_start_rent, null, false);
        addTopWindow(view);

        TextView txtStartRent = ButterKnife.findById(view, R.id.txtStartRent);
        txtStartRent.setText("До начала аренды осталось " + AppUtils.toTime(bookingTimeLeft) + getString(R.string.minutes));
    }

    private void showOpenPopup() {
        hideBottomWindow();

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
        } else {
            btnCloseOrOpen.setText(getString(R.string.close_car));
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
            ((TextView) ButterKnife.findById(view, R.id.txtTariff))
                    .setText(String.valueOf(mCar.getTariff().getUsage()));
            ((TextView) ButterKnife.findById(view, R.id.txtTotalUsage))
                    .setText(String.valueOf(mCheck.getUsageTotal()));
            ((TextView) ButterKnife.findById(view, R.id.txtParking))
                    .setText(String.valueOf(mCheck.getParkingCost()));
            ((TextView) ButterKnife.findById(view, R.id.txtTotal))
                    .setText(String.valueOf(mCheck.getTotal()));
        }
    }

    private class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
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
                        .setText("До начала аренды " + AppUtils.toTime(mBookingTimeLeft) + getString(R.string.minutes) + " минуты");
            }

            return view;
        }
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
            //TODO Что-то нужно сделать
        }
    }
}
