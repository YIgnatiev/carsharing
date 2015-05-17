package youdrive.today.login.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.dd.CircularProgressButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;
import youdrive.today.BaseActivity;
import youdrive.today.User;
import youdrive.today.maps.MapsActivity;
import youdrive.today.R;
import youdrive.today.login.LoginActionListener;
import youdrive.today.login.impl.LoginInteractorImpl;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class LoginActivity extends BaseActivity implements LoginActionListener {

    @InjectView(R.id.etLogin)
    EditText etLogin;

    @InjectView(R.id.etPassword)
    EditText etPassword;

    @InjectView(R.id.btnLogin)
    CircularProgressButton btnLogin;

    Handler mHandler = new Handler();

    private LoginInteractorImpl mInteractor;

    @OnClick(R.id.btnLogin)
    public void submit(View view) {
        btnLogin.setProgress(50);
        mInteractor.login(
                etLogin.getText().toString(),
                etPassword.getText().toString(),
                this);
    }

    @OnClick(R.id.txtRestore)
    public void restore(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtRegistration)
    public void registration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
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
        Timber.d("User " + user.toString());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                btnLogin.setProgress(100);
            }
        });

        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onError() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                btnLogin.setProgress(-1);
            }
        });
    }

    @Override
    public void onErrorNotFound() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                btnLogin.setProgress(-1);
            }
        });
    }

    @Override
    public void onErrorEmpty() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                btnLogin.setProgress(-1);
            }
        });
    }
}
