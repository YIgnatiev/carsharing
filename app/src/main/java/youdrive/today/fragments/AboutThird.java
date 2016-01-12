package youdrive.today.fragments;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.yandex.metrica.YandexMetrica;

import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.databinding.FragmentAboutThirdBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class AboutThird extends BaseFragment {

    private FragmentAboutThirdBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_about_third, container, false);
        b.setListener(this);
        setData();
        return b.getRoot();
    }

    private void setData() {
        b.tvMinuteTarification.setText(Html.fromHtml(getString(R.string.minute_tarrif_text)));
        b.tvFreeParking.setText(Html.fromHtml(getString(R.string.free_parking_text)));
        b.tvFreeFuel.setText(Html.fromHtml(getString(R.string.free_fuel_text)));
        b.tvFreeNight.setText(Html.fromHtml(getString(R.string.free_night_text)));
        b.tvFarRides.setText(Html.fromHtml(getString(R.string.far_rides_text)));
    }


    private void showTextDialog(String _message){
        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setMessage(_message)
                .setPositiveButton("OK", (num, window) -> {
                })
                .create();
        dialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_0_2");

        App.tracker().setScreenName("registration_0_2");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());

    }

    public void onMinuteTarrification(View view){
        showTextDialog(getString(R.string.minute_tarification));
    }
    public void onFreeParking(View view){
        showTextDialog(getString(R.string.free_parking));
    }

    public void onFuelIsPayed(View view){
        showTextDialog(getString(R.string.fuelIsPayed));
    }

    public void onFreeNightParking(View view){
        showTextDialog(getString(R.string.free_night_parking));
    }

    public void onFarRides(View view){
        showTextDialog(getString(R.string.far_rides));
    }

}
