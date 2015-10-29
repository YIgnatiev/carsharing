package youdrive.today.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import youdrive.today.R;
import youdrive.today.databinding.FragmentAboutFourthBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class AboutFourth extends BaseFragment {

    private FragmentAboutFourthBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_about_fourth ,container, false);
        return b.getRoot();
    }
}
