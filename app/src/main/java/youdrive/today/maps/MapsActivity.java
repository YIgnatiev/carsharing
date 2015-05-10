package youdrive.today.maps;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import youdrive.today.BaseActivity;
import youdrive.today.Car;
import youdrive.today.Command;
import youdrive.today.Menu;
import youdrive.today.R;
import youdrive.today.Status;
import youdrive.today.car.CarActionListener;
import youdrive.today.car.CarInteractorImpl;
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
    private Location mLastLocation;
    private MarkerOptions mMarker;
    private LocationRequest mLocationRequest;
    private float mZoomLevel;

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
    void onZoomIn(View v){
        if (mMap != null){
            if (mZoomLevel < mMap.getMaxZoomLevel()){
                mMap.animateCamera(CameraUpdateFactory.zoomTo(++mZoomLevel));
            }
        }
    }

    @OnClick(R.id.btnZoomOut)
    void onZoomOut(View v){
        if (mMap != null){
            if (mZoomLevel > mMap.getMinZoomLevel()){
                mMap.animateCamera(CameraUpdateFactory.zoomTo(--mZoomLevel));
            }
        }
    }

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
        mMapsInteractor.getStatusCars(this);

        mProgressDialog = new ProgressDialog(MapsActivity.this);
        mProgressDialog.setMessage("Please wait");

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
        if (mGoogleApiClient.isConnected()){
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
        items.add(new Menu(R.drawable.ic_ab_drawer, "Тарифы"));
        items.add(new Menu(R.drawable.ic_ab_drawer, "Помощь"));
        items.add(new Menu(R.drawable.ic_ab_drawer, "Позвонить оператору"));
        items.add(new Menu(R.drawable.ic_ab_drawer, "Выход"));
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
        Timber.d("Start Location Update");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        Timber.d("Stop Location Update");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    void updateLocation(Location location) {

        if (mMarker == null){
            mMarker = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Это Вы!")
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location));
            mMap.addMarker(mMarker);
        } else {
            Timber.d("Refresh Marker");
            mMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void showCarsDialog(final Car car){
        new MaterialDialog.Builder(MapsActivity.this)
                .title(car.getModel())
                .customView(R.layout.custom_info_contents, true)
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
                })
                .show();
    }

    private void showOpenDialog(final Car car){
        new MaterialDialog.Builder(MapsActivity.this)
                .title(car.getModel())
                .customView(R.layout.custom_info_contents, true)
                .positiveText("Открыть")
                .negativeText("Отменить")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mCarInteractor.command(Command.OPEN, MapsActivity.this);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                .show();
    }

    private void showCloseDialog(final Car car){
        new MaterialDialog.Builder(MapsActivity.this)
                .title(car.getModel())
                .customView(R.layout.custom_info_contents, true)
                .positiveText("Закрыть")
                .negativeText("Закончить")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mCarInteractor.command(Command.CLOSE, MapsActivity.this);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        mCarInteractor.complete(Command.COMPLETE, MapsActivity.this);
                    }
                })
                .show();
    }

//    private View mContainer;
    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {

            if (mMarkerCar.containsKey(marker)){
                if (mMarkerCar.size() > 1){
                    showCarsDialog(mMarkerCar.get(marker));
                } else {
                    if (Status.PARKING.equals(mCar.getStatus())
                            || Status.BOOKING.equals(mCar.getStatus())){
                        showOpenDialog(mMarkerCar.get(marker));
                    } else if (Status.USAGE.equals(mCar.getStatus())){
                        showCloseDialog(mMarkerCar.get(marker));
                    }
                }
            }

//            Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);
//            mContainer = findViewById(R.id.container);
//            mContainer.setAnimation(anim);
//            mContainer.setVisibility(View.VISIBLE);

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
    public void onComplete() {
        Timber.d("onComplete");
        mMapsInteractor.getStatusCars(this);
    }

    @Override
    public void onCars(List<Car> cars) {
        mCars = cars;

        //Сортировка по возрастанию поля walktime
        Collections.sort(mCars);

        //Сдвигаю камеру на ближайшую машину, если известна текущая позиция
        onMoveCamera(mCars.get(0));

        //Добавление маркеров на карту
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Car c : mCars) {
                    addMarker(c);
                }
            }
        });
    }

    private void onMoveCamera(final Car car) {
        if (mMarker != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                            new LatLngBounds(
                                    new LatLng(car.getLat(), car.getLon()),
                                    mMarker.getPosition()),
                            getPx(5)));
                    mZoomLevel = mMap.getCameraPosition().zoom;
                }
            });
        }
    }

    private int getPx(int dp){
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
        Timber.d("onToken");
        mToken = token;
        mCommand = command;
    }

    @Override
    public void onPleaseWait() {
        Timber.d("onPleaseWait");
        if (mToken != null
                && mCommand != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                    new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            Timber.d("ProgressDialog " + mProgressDialog);
                            if (mProgressDialog != null){
                                mProgressDialog.setMessage("Seconds remaining: " + millisUntilFinished / 1000);
                            }
                        }

                        public void onFinish() {
                            mProgressDialog.hide();
                            mCarInteractor.result(mCommand, mToken, MapsActivity.this);
                        }
                    }.start();
                }
            });
        }
    }

    @Override
    public void onErrorOpen() {
        Timber.d("onErrorOpen");
    }

    @Override
    public void onOpen() {
        Timber.d("onOpen");
        mCar.setStatus(Status.USAGE);
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
        Timber.d("Location LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
        updateLocation(location);

    }

//    private class CInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
//
//        private final View mView;
//
//        public CInfoWindowAdapter() {
//            mView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            return mView;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//    }
}
