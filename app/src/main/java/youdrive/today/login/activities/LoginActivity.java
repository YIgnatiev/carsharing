package youdrive.today.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.AppUtils;
import youdrive.today.BaseActivity;
import youdrive.today.User;
import youdrive.today.maps.MapsActivity;
import youdrive.today.R;
import youdrive.today.login.LoginActionListener;
import youdrive.today.login.impl.LoginInteractorImpl;

public class LoginActivity extends BaseActivity implements LoginActionListener {

    @InjectView(R.id.etLogin)
    MaterialEditText etLogin;

    @InjectView(R.id.etPassword)
    MaterialEditText etPassword;

    @InjectViews({R.id.etLogin, R.id.etPassword, R.id.txtRestore, R.id.txtAbout, R.id.txtRegistration})
    List<View> vInputs;

    @InjectView(R.id.btnLogin)
    CircularProgressButton btnLogin;

    private LoginInteractorImpl mInteractor;

    @OnClick(R.id.btnLogin)
    public void login() {
        if (btnLogin.getProgress() == 0
                && isValidate()) {

            ButterKnife.apply(vInputs, AppUtils.ENABLED, false);

            btnLogin.setProgress(50);
            mInteractor.login(
                    etLogin.getText().toString(),
                    etPassword.getText().toString(),
                    this);
        }
    }

    private boolean isValidate() {
        boolean isValidate = true;

        if (isEmpty(etLogin)) {
            etLogin.setError(getString(R.string.empty));
            isValidate = false;
        } else if (!etLogin.getText().toString().contains("@")) {
            etLogin.setError(getString(R.string.email_not_valid));
            isValidate = false;
        }

        if (isEmpty(etPassword)) {
            etPassword.setError(getString(R.string.empty));
            isValidate = false;
        }

        return isValidate;
    }

    private boolean isEmpty(MaterialEditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    @OnClick(R.id.txtRestore)
    public void restore(View view) {
        AppUtils.restore(this);
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        AppUtils.about(this);
    }

    @OnClick(R.id.txtRegistration)
    public void registration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mInteractor = new LoginInteractorImpl();
        btnLogin.setIndeterminateProgressMode(true);
    }

    @Override
    public void onSuccess(User user) {
        Timber.tag("Login").d("User " + user.toString());
        if (App.getInstance().getPreference() != null){
            App.getInstance().getPreference().putUser(new Gson().toJson(user));
        }

        AppUtils.success(btnLogin, getString(R.string.open_car));
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }

    @Override
    public void onError() {
        error(getString(R.string.internal_error));
    }

    @Override
    public void onErrorUserNotFound(final String message) {
        error(message);
    }

    @Override
    public void onErrorFieldEmpty(final String message) {
        error(message);
    }

    @Override
    public void onUnknownError(String text) {
        error(text);
    }

    private void error(String text) {
        ButterKnife.apply(vInputs, AppUtils.ENABLED, true);
        AppUtils.error(text, btnLogin);
    }

    @Override
    protected void onDestroy() {
        mInteractor.getSubscription().unsubscribe();
        super.onDestroy();
    }
}
