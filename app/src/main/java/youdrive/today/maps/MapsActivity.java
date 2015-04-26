package youdrive.today.maps;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener, CarActionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        Timber.tag("Maps");

        setActionBarIcon(R.drawable.ic_ab_drawer);
        setUpMapIfNeeded();

        lvProfile.addHeaderView(getLayoutInflater().inflate(R.layout.header_profile, null));
        lvProfile.setAdapter(new ProfileAdapter(this, R.layout.item_profile, getMenu()));
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        mProfileInteractor = new ProfileInteractorImpl();
        mCarInteractor = new CarInteractorImpl();

        new MapsInteractorImpl().getStatusCars(this);
        mProgressDialog = new ProgressDialog(MapsActivity.this);
        mProgressDialog.setMessage("Please wait");
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    void getCurrentLocation() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(onMarkerClickListener);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLat = location.getLatitude();
                mLon = location.getLongitude();
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
            }
        });
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
                    }
                })
                .show();
    }

//    private View mContainer;
    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {

            if (mMarkerCar.size() > 1){
                showCarsDialog(mMarkerCar.get(marker));
            } else {
                if (mCar.getStatus().equals(Status.PARKING)){
                    showOpenDialog(mMarkerCar.get(marker));
                } else if (mCar.getStatus().equals(Status.USAGE)){
                    showCloseDialog(mMarkerCar.get(marker));
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
                                .position(new LatLng(car.getLat(), car.getLon()))
                                .title(car.getModel())),
                car);
    }

    private void setUpMap() {
//        mMap.setInfoWindowAdapter(new CInfoWindowAdapter());
        getCurrentLocation();
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
        Timber.d("onInternalError");
        mCar.setStatus(Status.PARKING);
    }

    @Override
    public void onCars(List<Car> cars) {
        mCars = cars;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Car c : mCars) {
                    addMarker(c);
                }
            }
        });
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMarkerCar.clear();
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
