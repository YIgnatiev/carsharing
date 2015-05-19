package youdrive.today.login.activities;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import youdrive.today.BaseActivity;
import youdrive.today.R;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ConfirmationActivity extends BaseActivity{

    @OnClick(R.id.btnOk)
    public void ok(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.btnCancel)
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_confirmation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }
}
