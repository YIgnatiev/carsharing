package ru.lead2phone.ru.lead2phone.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.lead2phone.ru.lead2phone.R;
import ru.lead2phone.ru.lead2phone.rest.LeadToPhoneApi;
import ru.lead2phone.ru.lead2phone.utils.KeyboardUtil;
import ru.lead2phone.ru.lead2phone.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/26/15.
 */
public class LoginFragment extends BaseFragment implements Callback<String> {



    @Bind(R.id.etLogin_FragmentLogin)EditText etLogin;
    @Bind(R.id.etPassword_FragmentLogin)EditText etPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null);
        ButterKnife.bind(this, view);
        return view;
    }


    private boolean checkFields(){
        boolean isLoginCorrect = true;
        boolean isPasswordCorrect = true;
        if(etLogin.getText().toString().isEmpty()) {
            etLogin.setError(getString(R.string.enter_login));
            isLoginCorrect = false;
        }
        if(etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.enter_password));
            isPasswordCorrect = false;
        }
        return isLoginCorrect && isPasswordCorrect;
    }

    private void requestLogin(){
        mActivity.showProgres();
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        Preferences.setLogin(login);
        Preferences.setPassword(password);
        LeadToPhoneApi.getInstance(mActivity).login(login,password,this);
    }

    @OnClick(R.id.tvSubmit_FragmentLogin)
    public void onLoginSubmit(){
        KeyboardUtil.hideKeyBoard(etLogin,mActivity);
        KeyboardUtil.hideKeyBoard(etPassword,mActivity);
        if(checkFields()) requestLogin();
    }

    @OnClick(R.id.tvAbout_FragmentLogin)
    public void onAboutPressed(){
        mActivity.replaceFragment(new AboutFragment());
    }

    @OnClick(R.id.tvRegistration_FragmentLogin)
            public void onRegistrationPressed(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_registration)));
        startActivity(browserIntent);
    }

    @OnClick(R.id.tvForgotPassword_FragmentLogin)
            public void onRecoverPasswordPressed(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_recover_password)));
        startActivity(browserIntent);
    }






    @Override
    public void success(String s, Response response) {

        mActivity.hideProgress();


        if(s != null) {
            Preferences.setUserName(s);
            Preferences.saveIsLogged(true);
            mActivity.replaceFragmentWithoutBackStack(new ListFragment());

        }else{
            Toast.makeText(mActivity, R.string.wrong_name_or_password,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failure(RetrofitError error) {
        mActivity.hideProgress();
        Toast.makeText(mActivity, R.string.wrong_name_or_password,Toast.LENGTH_SHORT).show();
    }
}
