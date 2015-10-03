package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;

public class SplashActivity extends BaseActivity {



    @Override
    public void bindActivity() {
        DataBindingUtil.setContentView(this,R.layout.activity_splash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
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
        }, 3000);


    }
}
