package youdrive.today.maps;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import timber.log.Timber;
import youdrive.today.BaseActivity;
import youdrive.today.Car;
import youdrive.today.Item;
import youdrive.today.R;
import youdrive.today.profile.ProfileActionListener;
import youdrive.today.profile.ProfileAdapter;
import youdrive.today.profile.ProfileInteractorImpl;

public class MapsActivity extends BaseActivity implements MapsActionListener, ProfileActionListener {

    private GoogleMap mMap;
    private ProfileInteractorImpl mProfileInteractor;

    @InjectView(R.id.drawer)
    DrawerLayout mDrawer;

    @InjectView(R.id.lvProfile)
    ListView lvProfile;

    @OnItemClick(R.id.lvProfile)
    void onItemSelected(int position) {
        switch (position){
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
        new MapsInteractorImpl().getStatusCars(this);
    }

    private List<Item> getMenu() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(R.drawable.ic_ab_drawer, "Тарифы"));
        items.add(new Item(R.drawable.ic_ab_drawer, "Помощь"));
        items.add(new Item(R.drawable.ic_ab_drawer, "Позвонить оператору"));
        items.add(new Item(R.drawable.ic_ab_drawer, "Выход"));
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

    void getCurrentLocation(){
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(onMarkerClickListener);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
            }
        });
    }

    private View mContainer;
    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

//            new MaterialDialog.Builder(MapsActivity.this)
//                    .title("Skoda Rapid")
//                    .customView(R.layout.custom_info_contents, true)
//                    .positiveText("Забронировать")
//                    .negativeText("Отмена")
//                    .show();

            Animation anim = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bottom_up);
            mContainer = findViewById(R.id.container);
            mContainer.setAnimation(anim);
            mContainer.setVisibility(View.VISIBLE);

            return false;
        }
    };

    private void setUpMap() {
//        mMap.setInfoWindowAdapter(new CInfoWindowAdapter());
        mMap.addMarker(new MarkerOptions().position(new LatLng(55.764703, 37.561451)).title("Skoda Rapid"));
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
    public void onCars(List<Car> cars) {
        Timber.d("Cars " + cars.toString());
    }

    @Override
    public void onForbidden() {

    }

    @Override
    public void onTariffNotFound() {

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
