package youdrive.today.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import youdrive.today.BaseActivity;
import youdrive.today.R;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ThanksActivity extends BaseActivity {

    @OnClick(R.id.btnOk)
    public void auth(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_thanks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }
}
