package youdrive.today.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.yandex.metrica.YandexMetrica;

import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.databinding.FragmentAboutSecondBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class AboutSecond extends BaseFragment {
    private FragmentAboutSecondBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_about_second, container, false);
        return b.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_0_1");
        App.tracker().setScreenName("registration_0_1");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());
    }
}
