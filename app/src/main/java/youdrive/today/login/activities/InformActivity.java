package youdrive.today.login.activities;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import youdrive.today.BaseActivity;
import youdrive.today.R;

public class InformActivity extends BaseActivity {

    @OnClick(R.id.btnOk)
    public void auth(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_inform;
    }
}
