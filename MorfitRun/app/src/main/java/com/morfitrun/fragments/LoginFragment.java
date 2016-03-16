package com.morfitrun.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.morfitrun.R;
import com.morfitrun.activity.LoginActivity;
import com.morfitrun.activity.MainActivity;
import com.morfitrun.api.ErrorProccessor;
import com.morfitrun.api.RestClient;
import com.morfitrun.data_models.User;
import com.morfitrun.global.Constants;
import com.morfitrun.global.GlobalDataStorage;

import com.morfitrun.global.UserPreference;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 12.03.2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, Callback<User> {

    private FormEditText etLogin;
    private FormEditText etPassword;
    private FormEditText etForgotPasswordEmail;
    private TextView tvSubmit;
    private TextView tvRegister;
    private ImageView ivFacebook;
    private ImageView ivGooglePlus;
    private LoginActivity mActivity;
    private TextView tvForgotPassword ;
    private AlertDialog adForgotPassword;
    private String email;
    private String password;
    private boolean userPreferenseEmpty = false;


    @Override
    public void onAttach(Activity activity) {
        mActivity = (LoginActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        findViews(rootView);
        setListeners();
        verifyUserPreference();
        return rootView;
    }

    private void findViews(View _rootView) {
        tvSubmit = (TextView) _rootView.findViewById(R.id.tvLogIn_FL);
        tvRegister = (TextView) _rootView.findViewById(R.id.tvRegister_FL);
        tvForgotPassword = (TextView)_rootView.findViewById(R.id.tvForgotPassword_FL);
        etLogin = (FormEditText) _rootView.findViewById(R.id.etLogin_FL);
        etPassword = (FormEditText) _rootView.findViewById(R.id.etPassword_FL);
        ivFacebook = (ImageView) _rootView.findViewById(R.id.ivFacebook_FL);
        ivGooglePlus = (ImageView) _rootView.findViewById(R.id.ivGoogle_FL);
    }


    private void setListeners() {
        tvSubmit.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivFacebook.setOnClickListener(this);
        ivGooglePlus.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }

    private void verifyUserPreference(){
        email = UserPreference.getCurrentUserLogin(mActivity);
        password = UserPreference.getCurrentUserPassword(mActivity);
        if (!email.equals("") && !password.equals("")){
            final User user = new User(email, password);
            RestClient.getInstance().getSignInService().signIn(user, this);
            mActivity.showProgress(mActivity.getResources().getString(R.string.login_authorizing));
        }
        else userPreferenseEmpty = true;

    }

    private void login() {
        if (checkFields()) {
            email = etLogin.getText().toString();
            password = etPassword.getText().toString();
            User user = new User(email, password);

            RestClient.getInstance().getSignInService().signIn(user, this);
            mActivity.showProgress(mActivity.getResources().getString(R.string.login_authorizing));
        }
    }

    private void startRegisterFragment() {
        RegisterFragment fragment = new RegisterFragment();
        mActivity.replaceFragmentWithBackStack(fragment);
    }

    private boolean checkFields() {
        boolean allValid = true;
        FormEditText[] allFields = {etPassword, etLogin,};
        for (FormEditText field : allFields) {
            allValid = field.testValidity() && allValid;
            if (!field.testValidity()) {
                field.requestFocus();
            }
        }
        return allValid;
    }

    private void loginWithFacebook() {

    }

    private void loginWithGooglePlus() {

    }


    private void showDialog(String _message) {
        View viewGroup = mActivity.getLayoutInflater().inflate(R.layout.dialog_warning, new LinearLayout(mActivity));
        TextView textViewInfo = (TextView) viewGroup.findViewById(R.id.textView_warning_dialog);
        TextView textViewButton = (TextView) viewGroup.findViewById(R.id.textView_warning_dialog_ok);

        textViewInfo.setText(_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(viewGroup);

        final AlertDialog adDialog = builder.create();

        textViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adDialog.cancel();
            }
        });
        adDialog.show();
    }

    private void showForgotPasswordDialog() {
        View viewGroup = mActivity.getLayoutInflater().inflate(R.layout.dialog_forgot_password, new LinearLayout(mActivity));

        TextView tvCancel= (TextView) viewGroup.findViewById(R.id.tvCancel_DFP);
        TextView tvSend  = (TextView) viewGroup.findViewById(R.id.tvSend_DFP);
        etForgotPasswordEmail = (FormEditText)viewGroup.findViewById(R.id.fetEmail_DFP);
        tvCancel.setOnClickListener(this);
        tvSend.setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(viewGroup);

        adForgotPassword = builder.create();
        adForgotPassword.show();
    }

    private void requestForgotPassword() {
        Toast.makeText(mActivity, "requesting password", Toast.LENGTH_SHORT).show();
        adForgotPassword.dismiss();
    }


    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.tvLogIn_FL:
                login();
                break;
            case R.id.tvRegister_FL:
                startRegisterFragment();
                break;
            case R.id.ivFacebook_FL:
                loginWithFacebook();
                break;
            case R.id.ivGoogle_FL:
                loginWithGooglePlus();
                break;
            case R.id.tvForgotPassword_FL:
                showForgotPasswordDialog();
                break;
            case R.id.tvSend_DFP:
              if (etForgotPasswordEmail.testValidity()) requestForgotPassword();

                break;
            case R.id.tvCancel_DFP:
                adForgotPassword.dismiss();
                break;
        }
    }

    @Override
    public final void success(final User _user, final Response _response) {
        mActivity.hideProgress();
        GlobalDataStorage.setCurrentUser(_user);
        UserPreference.writeUserToPreferences(mActivity, email, password);
        startMainActivity(false);
    }

    @Override
    public final void failure(final RetrofitError _error) {
        mActivity.hideProgress();
        ErrorProccessor.processError(mActivity, _error);
        if (!userPreferenseEmpty)
            startMainActivity(true);
    }

    private void startMainActivity(final boolean _isOffline){
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra(Constants.MODE_OFFLINE, _isOffline);
        startActivity(intent);
        mActivity.stopLoginActivity();
    }
}


