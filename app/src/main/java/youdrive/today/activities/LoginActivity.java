package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Patterns;
import android.view.View;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.helpers.AppUtils;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.models.User;
import youdrive.today.databinding.ActivityLoginBinding;
import youdrive.today.listeners.LoginActionListener;
import youdrive.today.interceptors.LoginInteractorImpl;

public class LoginActivity extends BaseActivity implements LoginActionListener {

    private ActivityLoginBinding b;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        b.setListener(this);
        mInteractor = new LoginInteractorImpl();
        b.btnLogin.setIndeterminateProgressMode(true);
    }

    private LoginInteractorImpl mInteractor;

    //listener
    public void onLogin(View view) {
        if (b.btnLogin.getProgress() == 0 && isValidate()) {

            setEnabled(false);
            b.btnLogin.setProgress(50);
            mInteractor.login(b.etLogin.getText().toString(), b.etPassword.getText().toString(), this);
        }
    }

    public void onRestore(View view) {
        AppUtils.restore(this);
    }

    public void onAbout(View view) {
        AppUtils.about(this);
    }

    public void onRegistration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }

    private void setEnabled(boolean value) {
        b.etLogin.setEnabled(value);
        b.etPassword.setEnabled(value);
        b.txtRestore.setEnabled(value);
        b.txtAbout.setEnabled(value);
        b.txtRegistration.setEnabled(value);
    }

    private boolean isValidate() {
        boolean isPasswordTyped = true;
        boolean isEmailTyped = true;
        boolean isEmailValid = true;
        if (isEmpty(b.etLogin)) {
            b.etLogin.setError(getString(R.string.empty));
            isEmailTyped = false;
        } else if (!validateEmail(b.etLogin.getText().toString())) {

            b.etLogin.setError(getString(R.string.email_not_valid));
            isEmailValid = false;
        }

        if (isEmpty(b.etPassword)) {
            b.etPassword.setError(getString(R.string.empty));
            isPasswordTyped = false;
        }

        return isPasswordTyped && isEmailTyped && isEmailValid;
    }

    private boolean isEmpty(MaterialEditText et) {
        return et.getText().toString().isEmpty();
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onSuccess(User user) {
        Timber.tag("Login").d("User " + user.toString());
        if (App.getInstance().getPreference() != null) {
            App.getInstance().getPreference().putUser(new Gson().toJson(user));
        }

        AppUtils.success(b.btnLogin, getString(R.string.open_car));
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
        setEnabled(true);
        AppUtils.error(text, b.btnLogin);
    }

    @Override
    protected void onDestroy() {
        mInteractor.getSubscription().unsubscribe();
        super.onDestroy();
    }
}
