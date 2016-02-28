package com.morfitrun.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.morfitrun.R;
import com.morfitrun.data_models.User;
import com.morfitrun.fragments.SplashFragment;
import com.morfitrun.global.Constants;
import com.morfitrun.fragments.LoginFragment;
import com.morfitrun.global.ProgressDialogManager;
import com.morfitrun.global.UserPreference;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 12.03.2015.
 */
public class LoginActivity extends Activity {

    private ProgressDialogManager pbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CookieHandler.setDefault(new CookieManager());
        pbManager = new ProgressDialogManager(this);
        if (isLogOut())
            startLoginFragment();
        else
            startSplashFragment();
        showLoginFragmentDelay();
    }

    private boolean isLogOut(){
        final Intent intent = getIntent();
        return intent.getBooleanExtra(Constants.LOG_OUT, false);
    }

    private void showLoginFragmentDelay(){
        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                startLoginFragment();
            }
        };
        worker.schedule(task, 2, TimeUnit.SECONDS);
    }

    private void startSplashFragment(){
        SplashFragment fragment = new SplashFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer_AL, fragment)
                .commitAllowingStateLoss();
    }

    private void startLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer_AL, fragment)
                .commitAllowingStateLoss();
    }

    public void replaceFragmentWithBackStack(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.flContainer_AL, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

//    public void writeUserToPreferences(User user) {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(Constants.USER_LOGIN, user.getLogin());
//        editor.putString(Constants.USER_PASSWORD, user.getPassword());
//        editor.apply();
//    }

    public void stopLoginActivity(){
        finish();
    }

    public final void showProgress(final String _message) {
        pbManager.showProgressDialog(_message);
    }

    public final void hideProgress() {
        pbManager.hideProgressDialog();
    }
}







