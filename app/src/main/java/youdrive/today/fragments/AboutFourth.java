package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import youdrive.today.R;
import youdrive.today.activities.LoginActivity;
import youdrive.today.activities.RegistrationNewActivity;
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
        b.setListener(this);
        return b.getRoot();
    }




    public void onRegister(View view){
        startActivity(new Intent(getActivity(), RegistrationNewActivity.class));
        getActivity().finish();
    }





public void onLogin(View view){
    startActivity(new Intent(getActivity(), LoginActivity.class));
    getActivity().finish();
}



    public void onCommunity(View view){
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://youdrive.copiny.com")));

    }


}
