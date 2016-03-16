package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityInformBinding;

public class InformActivity extends BaseActivity {
    private ActivityInformBinding b;
  //listener
    public void auth(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_inform);
        b.setListener(this);
    }
}
