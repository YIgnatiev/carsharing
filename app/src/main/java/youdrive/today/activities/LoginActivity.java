package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Patterns;
import android.view.View;

import com.google.gson.Gson;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import timber.log.Timber;
import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityLoginBinding;
import youdrive.today.helpers.AppUtils;
import youdrive.today.interceptors.LoginInteractorImpl;
import youdrive.today.listeners.LoginActionListener;
import youdrive.today.models.User;

public class LoginActivity extends BaseActivity implements LoginActionListener {
    private LoginInteractorImpl mInteractor;
    private ActivityLoginBinding b;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        b.setListener(this);
        mInteractor = new LoginInteractorImpl();
        b.btnLogin.setIndeterminateProgressMode(true);
        b.btnLogin.setEnabled(false);
       checkFields();
    }

    //listener
    public void onLogin(View view) {
        if (b.btnLogin.getProgress() == 0 && isConnected()) {
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
        startActivity(new Intent(this, RegistrationNewActivity.class));
        finish();
    }

    private void setEnabled(boolean value) {
        b.etLogin.setEnabled(value);
        b.etPassword.setEnabled(value);
        b.txtRestore.setEnabled(value);
        b.txtAbout.setEnabled(value);
        b.txtRegistration.setEnabled(value);
    }

    private boolean isConnected() {
        boolean isConnected = isNetworkConnected();
        if (!isConnected) {
            showToast(getString(R.string.no_internet));
        }
        return isConnected;
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

    public void checkFields() {
        Observable<Boolean> email = WidgetObservable
                .text(b.etLogin)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> validateEmail(t.toString()))
                .doOnNext(bool -> {
                    if (!bool) b.etLogin.setError(getString(R.string.email_not_valid));
                });

        Observable<Boolean> password = WidgetObservable.text(b.etPassword)
                .distinctUntilChanged()
                .map(OnTextChangeEvent::text)
                .map(t -> t.length() != 0)
                .doOnNext(bool -> {
                    if (!bool) b.etPassword.setError(getString(R.string.empty));
                });

        Observable.combineLatest(email, password, (a, b) -> a && b)
                .distinctUntilChanged()
                .subscribe(b.btnLogin::setEnabled);
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
