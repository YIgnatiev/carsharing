package youdrive.today.activities;

import android.databinding.DataBindingUtil;
import android.view.View;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityConfirmationBinding;

public class ConfirmationActivity extends BaseActivity{
    ActivityConfirmationBinding b;
    //listener
    public void onOk(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void bindActivity() {
      b= DataBindingUtil.setContentView(this,R.layout.activity_confirmation);
        b.setListener(this);
    }
}
