package youdrive.today.activities;

import android.content.Intent;
import android.os.Bundle;

import youdrive.today.App;
import youdrive.today.BaseActivity;

public class SplashActivity extends BaseActivity {


    @Override
    public void bindActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (App.getInstance().getPreference().isFirst() && App.getInstance().getPreference().getUser() == null) {
            startActivity(new Intent(this, WellcomeActivity.class));
        } else if (App.getInstance().getPreference().getUser() != null) {
            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }

        finish();
    }
}
