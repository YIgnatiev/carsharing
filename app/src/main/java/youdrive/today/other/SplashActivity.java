package youdrive.today.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.login.activities.LoginActivity;
import youdrive.today.login.activities.RegistrationActivity;
import youdrive.today.maps.MapsActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (App.getInstance().getPreference() != null) {
                    if (App.getInstance().getPreference().isInvite()) {
                        App.getInstance().getPreference().putInvite(false);
                        startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
                    } else if (App.getInstance().getPreference().getSession() != null) {
                        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }
                finish();
            }
        }, 3000);


    }
}
