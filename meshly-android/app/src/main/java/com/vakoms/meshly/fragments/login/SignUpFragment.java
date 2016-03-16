package com.vakoms.meshly.fragments.login;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.LoginActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentSignupBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */
public class SignUpFragment extends BaseFragment<LoginActivity> {

    private FragmentSignupBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            P.clearData();
            b = FragmentSignupBinding.inflate(inflater);
            b.setListener(this);
            KeyboardUtil.showKeyBoard(b.etEmail, getActivity());
        }
        return b.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }


    private void hideKeyboard() {
        KeyboardUtil.hideKeyBoard(b.etEmail, getActivity());
        KeyboardUtil.hideKeyBoard(b.etPassword, getActivity());
        KeyboardUtil.hideKeyBoard(b.etPasswordAgain, getActivity());
    }


    private boolean checkFields() {
        boolean isTypedEmail = true;
        boolean isTypedPassword = true;
        boolean isTypedPasswordAgain = true;
        boolean isEmailValid = true;
        boolean isPasswordsMatch = true;


        if (TextUtils.isEmpty(b.etEmail.getText().toString())) {
            b.etEmail.setError("Empty email field");
            isTypedEmail = false;
        }
        if (TextUtils.isEmpty(b.etPassword.getText().toString())) {
            b.etPassword.setError("Empty password field");
            isTypedPassword = false;
        }
        if (TextUtils.isEmpty(b.etPasswordAgain.getText().toString())) {
            b.etPasswordAgain.setError("Empty password field");
            isTypedPasswordAgain = false;
        }

        if (!isValidEmail(b.etEmail.getText().toString())) {
            b.etEmail.setError("Email is invalid");
            isEmailValid = false;
        }

        if (!(b.etPassword.getText().toString().equals(b.etPasswordAgain.getText().toString()))) {
            b.etPassword.setError("Password doesn't match");
            b.etPasswordAgain.setError("Password doesn't match");
            isPasswordsMatch = false;
        }
        return isTypedEmail && isTypedPassword && isTypedPasswordAgain && isEmailValid && isPasswordsMatch;
    }


    private void signUp() {

        hideKeyboard();
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .meshly()
                .checkEmail(b.etEmail.getText().toString())
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCheckEmailSuccess,this::handleError);

        mSubscriptions.add(subscription);
    }


    public void handleError(Throwable error) {
     mActivity.handleError(error);

    }

    private void onCheckEmailSuccess(BaseResponse response) {
        mActivity.stopProgress();
        if (response.getStatus() == 1) {

            UserMe user = new UserMe();
            user.setPassword(b.etPassword.getText().toString());
            user.setEmail(b.etEmail.getText().toString());

            mActivity.replaceFragmentLeft(SignupEditFragment.getInstance(user));

        } else {
            BaseResponse.Error error = (BaseResponse.Error) response.getError().get(0);
            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public void onBackClicked(View view) {
        mActivity.onBackPressed();
    }

    public void onDoneClicked(View view) {
        if (checkFields()) signUp();
    }





}
