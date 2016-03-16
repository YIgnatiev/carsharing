package com.vakoms.meshly.fragments.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.LoginActivity;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Token;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.databinding.FragmentLoginBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 11.05 .2015.
 * tajcig@ya.ru
 */
public class LocalLoginFragment extends BaseFragment<LoginActivity> {

    private FragmentLoginBinding b;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = FragmentLoginBinding.inflate(inflater);
            b.setListener(this);
        }
        return b.getRoot();
    }



    //listeners
    public void onDoneClicked(View view) {

        if (checkFields()) {
            KeyboardUtil.hideKeyBoard(b.etEmail, getActivity());
           loginUser();
        }

    }




    //network
    private void loginUser() {
        mActivity.showProgress();

        Subscription subscription= RetrofitApi
                .getInstance()
                .meshly()
                .loginLocalUser(b.etEmail.getText().toString(), b.etPassword.getText().toString())
                .timeout(3, TimeUnit.SECONDS)
                .flatMap(this::handleTokens)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoginSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }

    private  Observable<BaseResponse<UserMe>> handleTokens(Token token){
        if (token != null) {
            P.saveTokens(token.getLoginToken(), token.getRefreshToken(), token.getLoginExpiresIn(), token.getLoginTokenType());
            return RetrofitApi.getInstance().user().getRegisteredUserDetails();
        } else return Observable.error(new Exception());
    }


    public  void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    public void onLoginSuccess(BaseResponse<UserMe> userMe){
        mActivity.stopProgress();
        UserDAO dao = UserDAO.getInstance();
        dao.saveUserMe(mActivity.getContentResolver(), userMe.getData());
        mActivity.checkIfWellcomeScreenNeeded();
    }


    public void onBackClicked(View view) {
        mActivity.onBackPressed();
    }

    public void onForgotPasswordClicked(View view) {
        mActivity.replaceFragmentLeft(new ForgotPasswordFragment());
    }

    public void onSignUpClicked(View view) {
        mActivity.replaceFragmentLeft(new SignUpFragment());
    }


    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideKeyBoard(b.etEmail, getActivity());
    }


    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkFields() {
        boolean isValidEmail = true;
        boolean isPasswordEntered = true;
        boolean isEmailEntered = true;


        if (TextUtils.isEmpty(b.etEmail.getText().toString())) {
            b.etEmail.setError("Email is empty");
            isEmailEntered = false;
        }
        if(TextUtils.isEmpty(b.etPassword.getText().toString())){
            b.etPassword.setError("Password is empty");
            isPasswordEntered = false;
        }

        if (!isValidEmail(b.etEmail.getText())) {
            b.etEmail.setError("Invalid email");
            isValidEmail = false;
        }

    return isValidEmail && isPasswordEntered && isEmailEntered;
    }

}