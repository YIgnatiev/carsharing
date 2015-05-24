package youdrive.today.maps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import timber.log.Timber;
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
import youdrive.today.other.OrderCarActivity;
import youdrive.today.profile.ProfileActionListener;
import youdrive.today.profile.ProfileAdapter;
import youdrive.today.profile.ProfileInteractorImpl;

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener, CarActionListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private ProfileInteractorImpl mProfileInteractor;

    Handler mHandler = new Handler();

    @InjectView(R.id.drawer)
    DrawerLayout mDrawer;

    @InjectView(R.id.lvProfile)
    ListView lvProfile;

    @InjectView(R.id.ltContainer)
    FrameLayout ltContainer;

    private String mToken;
    private CarInteractorImpl mCarInteractor;
    private Car mCar;
    private Command mCommand;
    private MapsInteractorImpl mMapsInteractor;
    private GoogleApiClient mGoogleApiClient;
    private MarkerOptions mMarker;
    private LocationRequest mLocationRequest;
    private float mZoomLevel;
    private CircularProgressButton btnOpen;
    private CircularProgressButton btnCloseCar;
    private CircularProgressButton btnCloseRent;
    private CircularProgressButton btnCancel;
    private Check mCheck;

    private CircularProgressButton btnBook;
    private Location mLastLocation;

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

    @InjectView(R.id.txtInfo)
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        Timber.tag("Maps");

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
        items.add(new Menu(R.drawable.icon_tariff, "Тарифы"));
        items.add(new Menu(R.drawable.icon_help, "Помощь"));
        items.add(new Menu(R.drawable.icon_call, "Позвонить оператору"));
        items.add(new Menu(R.drawable.icon_exit, "Выход"));
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
            mMap.setOnMarkerClickListener(onMarkerClickListener);
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
        MaterialDialog dialog = new MaterialDialog.Builder(MapsActivity.this)
                .customView(R.layout.dialog_info_contents, true)
                .widgetColorRes(R.color.white)
                .show();

        View view = dialog.getCustomView();
        if (view != null) {
            buildDialog(view, car);
        }
    }

    private void buildDialog(View view, final Car car) {
        ((TextView) ButterKnife.findById(view, R.id.txtModel))
                .setText(car.getModel());
        ((TextView) ButterKnife.findById(view, R.id.txtDistance))
                .setText(convertKilometers(car.getDistance()));
        ((TextView) ButterKnife.findById(view, R.id.txtTimeTo))
                .setText(convertTime(car.getWalktime()));
        ((TextView) ButterKnife.findById(view, R.id.txtType))
                .setText(car.getTransmission());
        ((TextView) ButterKnife.findById(view, R.id.txtTaxDrive))
                .setText(String.valueOf(car.getTariff().getUsage()));
        ((TextView) ButterKnife.findById(view, R.id.txtTaxPark))
                .setText(String.valueOf(car.getTariff().getParking()));
        btnBook = ButterKnife.findById(view, R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (car.getId() != null
                        && mLastLocation.getLatitude() > 0.0d
                        && mLastLocation.getLongitude() > 0.0d) {
                    mCarInteractor.order(car.getId(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
                } else {
                    Toast.makeText(MapsActivity.this, "Не удалось установить месторасположение", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    String convertKilometers(int meters) {
        return meters * 0.001 + " " + getString(R.string.km);
    }

    String convertTime(int seconds) {
        long minute = TimeUnit.SECONDS.toMinutes(seconds);
        return minute + " " + getString(R.string.minutes);
    }

    private void showOpenDialog() {
        showOpenPopup();
    }

    private void showCloseDialog() {
        showClosePopup();
    }


    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {

            if (mMarkerCar.containsKey(marker)) {
                if (mMarkerCar.size() > 1) {
                    showCarsDialog(mMarkerCar.get(marker));
                } else {
                    mMap.setInfoWindowAdapter(new CInfoWindowAdapter());
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
        Timber.d("ERROR");
    }

    @Override
    public void onAccessDenied() {
        Timber.d("onAccessDenied");
    }

    @Override
    public void onCommandNotSupported() {
        Timber.d("onCommandNotSupported");
    }

    @Override
    public void onTokenNotFound() {
        Timber.d("onTokenNotFound");
    }

    @Override
    public void onInternalError() {
        Timber.d("onInternalError");
    }

    @Override
    public void onClose() {
        Timber.tag("Action").d("onClose");
        onStatus(Status.PARKING);
    }

    @Override
    public void onComplete(Check check) {
        Timber.tag("Action").d("onComplete");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                hideWindow();
            }
        });
        startActivity(new Intent(this, CompleteActivity.class).putExtra("check", check));
        mMapsInteractor.getStatusCar(this);
    }

    @Override
    public void onBookingTimeLeft(int bookingTimeLeft) {
        Timber.tag("Action").d("onBookingTimeLeft " + bookingTimeLeft);
        //TODO
    }

    @Override
    public void onCars(List<Car> cars) {
        Collections.sort(cars);
        onMoveCamera(cars.get(0));
        for (Car c : cars) {
            addMarker(c);
        }

        txtInfo.setText("До ближайшей машины " + convertTime(cars.get(0).getWalktime()) + " пешком");
    }

    private void onMoveCamera(final Car car) {
        if (mMarker != null) {
            mHandler.post(new Runnable() {
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

    private int getPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onForbidden() {
        Timber.d("onForbidden");
    }

    @Override
    public void onTariffNotFound() {
        Timber.d("onTariffNotFound");
    }

    @Override
    public void onOrder(final Car car) {
        Timber.tag("Action").d("onOrder");
        mCar = car;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMarkerCar.clear();
                mMap.clear();

                //TODO Обработка статуса в Interactor-e

                onStatus(Status.BOOKING);
                addMarker(car);

                startActivity(new Intent(MapsActivity.this, OrderCarActivity.class).putExtra("car", mCar));
            }
        });

    }

    @Override
    public void onCar(Car car) {
        Timber.tag("Action").d("OnCar " + car.toString());
        mCar = car;
        addMarker(car);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(car.getLat(), car.getLon()),
                15f), 1500, null);
    }

    @Override
    public void onStatus(final Status status) {
        Timber.tag("Action").d("onStatus " + status.toString());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Status.PARKING.equals(status)
                        || Status.BOOKING.equals(status)) {
                    showOpenDialog();
                } else if (Status.USAGE.equals(status)) {
                    showCloseDialog();
                }
            }
        });
    }

    @Override
    public void onCheck(Check check) {
        Timber.tag("Action").d("Check " + check.toString());
        mCheck = check;

    }

    @Override
    public void onCarNotFound() {
        Timber.d("onCarNotFound");
    }

    @Override
    public void onNotInfo() {
        Timber.d("onNotInfo");
    }

    @Override
    public void onNotOrder() {
        Timber.d("onNotOrder");
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
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCarInteractor.result(mCommand, mToken, MapsActivity.this);
                }
            }, 5000);
        }
    }

    @Override
    public void onErrorOpen() {
        Timber.d("onErrorOpen");
    }

    @Override
    public void onOpen() {
        Timber.d("onOpen");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                btnOpen.setProgress(100);
                onStatus(Status.USAGE);
                hideWindow();
                showClosePopup();
            }
        });

    }

    @Override
    public void onSessionNotFound() {
        Timber.d("SessionNotFound");
    }

    @Override
    public void onInvalidRequest() {
        Timber.d("InvalidRequest");
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
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    private void addWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);

        ltContainer.setAnimation(anim);
        ltContainer.addView(view);
    }

    private void hideWindow() {
        ltContainer.removeAllViews();
    }

    private void showOpenPopup() {

        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popup_open_car, null, false);

        addWindow(view);

        btnOpen = ButterKnife.findById(view, R.id.btnOpen);
        btnOpen.setIndeterminateProgressMode(true);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOpen.setProgress(50);
                mCarInteractor.command(Command.OPEN, MapsActivity.this);
            }
        });

        btnCancel = ButterKnife.findById(view, R.id.btnCancel);
        btnCancel.setIndeterminateProgressMode(true);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancel.setProgress(50);
//                mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
            }
        });
    }

    private void showClosePopup() {
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_close_car, null, false);

        addWindow(view);

        btnCloseCar = ButterKnife.findById(view, R.id.btnCloseCar);
        btnCloseCar.setIndeterminateProgressMode(true);
        btnCloseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCloseCar.setProgress(50);
                mCarInteractor.command(Command.CLOSE, MapsActivity.this);
            }
        });

        btnCloseRent = ButterKnife.findById(view, R.id.btnCloseRent);
        btnCloseRent.setIndeterminateProgressMode(true);
        btnCloseRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCloseRent.setProgress(50);
                mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
            }
        });

        if (mCheck != null) {
            TextView txtTariff = ButterKnife.findById(view, R.id.txtTariff);
            txtTariff.setText(String.valueOf(mCar.getTariff().getUsage()));

            TextView txtTotalUsage = ButterKnife.findById(view, R.id.txtTotalUsage);
            txtTotalUsage.setText(String.valueOf(mCheck.getUsageTotal()));

            TextView txtParking = ButterKnife.findById(view, R.id.txtParking);
            txtParking.setText(String.valueOf(mCheck.getParkingCost()));

            TextView txtTotal = ButterKnife.findById(view, R.id.txtTotal);
            txtTotal.setText(String.valueOf(mCheck.getTotal()));
        }
    }

    private class CInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.marker_info, null);

            TextView txtModel = (TextView) view.findViewById(R.id.txtModel);
            txtModel.setText(mCar.getModel());

            TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            txtNumber.setText(mCar.getNumber());

            TextView txtColor = (TextView) view.findViewById(R.id.txtColor);
            txtColor.setText(mCar.getColor());

            TextView txtStartUsage = (TextView) view.findViewById(R.id.txtStartUsage);
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        mMapsInteractor.getSubscription().unsubscribe();
        super.onDestroy();
    }
}
