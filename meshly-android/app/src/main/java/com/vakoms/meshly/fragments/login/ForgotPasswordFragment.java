package com.vakoms.meshly.fragments.login;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.LoginActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentForgotPasswordBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */
public class ForgotPasswordFragment extends BaseFragment<LoginActivity>{

    private FragmentForgotPasswordBinding b ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = FragmentForgotPasswordBinding.inflate(inflater);
            b.setListener(this);
            KeyboardUtil.showKeyBoard(b.etEmail, mActivity);
        }
        return b.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideKeyBoard(b.etEmail, mActivity);
    }

    //listener
    public void onDoneClicked(View view){
        if(checkFields()) restorePassword();
    }

    public void onBackClicked(View view){
        getActivity().onBackPressed();
    }

    private boolean checkFields(){
       if (TextUtils.isEmpty(b.etEmail.getText().toString())) {
           b.etEmail.setError("Field is empty");
           return false;
       }
       if (!isValidEmail(b.etEmail.getText().toString())) {
           b.etEmail.setError("Email is invalid");
           return false;
       }
       return true;
   }




    private void restorePassword() {

        KeyboardUtil.hideKeyBoard(b.etEmail, mActivity);
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                        .getInstance()
                        .meshly()
                        .forgotPassword(b.etEmail.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onForgotPasswordSuccess,this::handleError);

        mSubscriptions.add(subscription);

    }




    public boolean isValidEmail(CharSequence target) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public void onForgotPasswordSuccess(BaseResponse response) {
        mActivity.stopProgress();

        if(!response.getError().isEmpty()) {
            BaseResponse.Error error = (BaseResponse.Error)response.getError().get(0);

            Toast.makeText(getActivity(),error.getMessage() , Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"Check your email", Toast.LENGTH_SHORT).show();
            mActivity.onBackPressed();
        }
    }

    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

}
