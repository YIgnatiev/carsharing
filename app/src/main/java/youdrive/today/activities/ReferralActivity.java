package youdrive.today.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityReferalBinding;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ReferralActivity extends BaseActivity {

    ActivityReferalBinding b;


    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_referal);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
