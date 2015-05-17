package youdrive.today.maps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import butterknife.Optional;
import timber.log.Timber;
import youdrive.today.App;
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

    private List<Car> mCars;

    private double mLat = 0.0d;
    private double mLon = 0.0d;

    private String mToken;
    private ProgressDialog mProgressDialog;
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

        mProgressDialog = new ProgressDialog(MapsActivity.this);
        mProgressDialog.setMessage("Пожалуйста подождите...");

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
        mLat = location.getLatitude();
        mLon = location.getLongitude();
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
                .title(car.getModel())
                .customView(R.layout.dialog_info_contents, true)
                .positiveText("Забронировать")
                .negativeText("Отмена")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (car.getId() != null
                                && mLat > 0.0d
                                && mLon > 0.0d) {
                            Timber.d("Get Order");
                            mCarInteractor.order(car.getId(), mLat, mLon, MapsActivity.this);
                        } else {
                            Toast.makeText(MapsActivity.this, "Не удалось установить месторасположение", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).show();

        View view = dialog.getCustomView();
        if (view != null) {
            buildDialog(view, car);
        }
    }

    private void buildDialog(View view, Car car) {
        DialogHolder holder = new DialogHolder(view);
        holder.txtDistanse.setText(convertKilometers(car.getDistance()));
        holder.txtTimeTo.setText(convertTime(car.getWalktime()));
        holder.txtType.setText(car.getTransmission());
        holder.txtTaxDrive.setText(String.valueOf(car.getTariff().getUsage()));
        holder.txtTaxPark.setText(String.valueOf(car.getTariff().getParking()));
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
                    if (Status.PARKING.equals(mCar.getStatus())
                            || Status.BOOKING.equals(mCar.getStatus())) {
                        showOpenDialog();
                    } else if (Status.USAGE.equals(mCar.getStatus())) {
                        showCloseDialog();
                    }
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
        Timber.d("onClose");
        mCar.setStatus(Status.PARKING);
    }

    @Override
    public void onComplete(Check check) {
        Timber.d("onComplete");
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
    public void onCars(List<Car> cars) {
        mCars = cars;

        Collections.sort(mCars);
        onMoveCamera(mCars.get(0));
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Car c : mCars) {
                    addMarker(c);
                }

                txtInfo.setText("До ближайшей машины " + convertTime(mCars.get(0).getWalktime()) + " пешком");
            }
        });
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
        mCar = car;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMarkerCar.clear();
                mMap.clear();

                //TODO Обработка статуса в Interactor-e

                mCar.setStatus(Status.BOOKING);
                addMarker(car);

                startActivity(new Intent(MapsActivity.this, OrderCarActivity.class).putExtra("car", mCar));
            }
        });

    }

    @Override
    public void onCar(final Car car) {
        mCar = car;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                addMarker(car);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(car.getLat(), car.getLon()),
                        15f), 1500, null);
            }
        });
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
                mCar.setStatus(Status.USAGE);
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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdates();
        if (mLastLocation != null && mMarkerCar.isEmpty()) {
            updateLocation(mLastLocation);
            mMapsInteractor.getStatusCars(mLastLocation.getLatitude(), mLastLocation.getLongitude(), MapsActivity.this);
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

    static class DialogHolder {

        @InjectView(R.id.txtDistance)
        TextView txtDistanse;

        @InjectView(R.id.txtTimeTo)
        TextView txtTimeTo;

        @InjectView(R.id.txtType)
        TextView txtType;

        @InjectView(R.id.txtTaxDrive)
        TextView txtTaxDrive;

        @InjectView(R.id.txtTaxPark)
        TextView txtTaxPark;

        public DialogHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private void addWindow(View view) {
        Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);
        ltContainer.setAnimation(anim);
        ltContainer.addView(view);
    }

    private void hideWindow(){
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
    }

    private void showClosePopup() {
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.popup_close_car, null, false);

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

}
