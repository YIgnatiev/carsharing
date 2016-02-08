package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.yandex.metrica.YandexMetrica;

import java.util.Collections;
import java.util.List;

import rx.subscriptions.CompositeSubscription;
import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.activities.LoginActivity;
import youdrive.today.activities.RegistrationActivity;
import youdrive.today.activities.WellcomeActivity;
import youdrive.today.databinding.FragmentAboutFourthBinding;
import youdrive.today.interceptors.MapsInteractorImpl;
import youdrive.today.models.Coord;
import youdrive.today.models.SimpleCar;
import youdrive.today.response.PolygonResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class AboutFourth extends BaseFragment<WellcomeActivity> {
    private GoogleMap mMap;
    private PolygonOptions mPolygon;
    private FragmentAboutFourthBinding b;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_about_fourth, container, false);
        b.setListener(this);
        initTextView();
        b.map.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());
        b.map.getMapAsync(this::initMap);
        return b.getRoot();
    }

    private void initTextView() {
        b.tvFooter.setText(Html.fromHtml(getString(R.string.have_questions)));
        b.tvFooter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initMap(GoogleMap map) {
        mMap = map;
        mActivity.getLocationPermission(this::setMyLocation);
    }

    private void setMyLocation() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        LatLng moscow = new LatLng(55.749792, 37.632495);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(moscow, 11);
        mMap.animateCamera(cameraUpdate);
        getCars();
    }

    public void onRegister(View view) {
        startActivity(new Intent(getActivity(), RegistrationActivity.class));
        getActivity().finish();
    }

    public void onLogin(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onResume() {
        b.map.onResume();
        mSubscriptions = new CompositeSubscription();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSubscriptions.unsubscribe();
        super.onPause();
        b.map.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_0_3");
        App.tracker().setScreenName("registration_0_3");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        b.map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        b.map.onLowMemory();
    }

    private void getCars() {
        MapsInteractorImpl interactor = new MapsInteractorImpl();

        if (mActivity.isNetworkConnected()) {
            interactor.getInfo(this::onCarsSuccess, this::onCarsFailure, mSubscriptions);
        } else {

            mActivity.showMessage(getString(R.string.no_internet));
        }
    }

    private void onCarsSuccess(PolygonResponse response) {

        List<SimpleCar> cars = response.getCars();
        Collections.sort(cars, (left, right) -> Double.compare(left.getLatitude(), right.getLatitude()));
        for (SimpleCar c : cars) {
            addMarker(c);
        }
        for (List<Coord> coords : response.getArea()){
            drawPolygon(coords);
        }

        if (cars.size() != 1) {
            CameraUpdateFactory.newLatLngZoom(new LatLng(cars.get(0).getLatitude(), cars.get(0).getLongitude()), 14);
        } else {
            CameraUpdateFactory.newLatLngZoom(new LatLng(cars.get(0).getLatitude(), cars.get(0).getLongitude()), 14);
        }
    }


    private void drawPolygon(List<Coord> coordList) {

        mPolygon = new PolygonOptions()
                .fillColor(getResources().getColor(R.color.polygonColor))
                .strokeColor(getResources().getColor(android.R.color.transparent))
                .geodesic(true);

        for (Coord coord : coordList)
            mPolygon.add(coord.toLatLng());
        mPolygon.add(coordList.get(0).toLatLng());
        mMap.addPolygon(mPolygon);


    }

    private void onCarsFailure(Throwable t) {
        mActivity.showErrorMessage(getString(R.string.error));
    }

    private int getPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    private void addMarker(final SimpleCar car) {


        Glide.with(mActivity)
                .load(car.getPin_img() + "_android.png")
                .asBitmap()
                .sizeMultiplier(0.5f)
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(car.getLatitude(), car.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromBitmap(resource));
                        mMap.addMarker(markerOptions);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        MarkerOptions markerOptions = new MarkerOptions()
                                .flat(true)
                                .position(new LatLng(car.getLatitude(), car.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_location));
                        mMap.addMarker(markerOptions);
                    }
                });
    }
}
