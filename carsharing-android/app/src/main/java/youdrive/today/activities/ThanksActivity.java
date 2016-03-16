package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityThanksBinding;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ThanksActivity extends BaseActivity {
    ActivityThanksBinding b;
    //listener
    public void onAuth(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_thanks);
        b.setListener(this);
    }
}
