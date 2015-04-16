package youdrive.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by psuhoterin on 16.04.15.
 */
public class ConfirmationActivity extends BaseActivity implements ConfirmationActionListener{

    public static final String REQUEST = "youdrive.today.REQUEST";
    private ConfirmationInteractorImpl mInteractor;

    @OnClick(R.id.btnOk)
    public void ok(View view) {
        mInteractor.invite(getIntent().getStringExtra(REQUEST), this);
    }

    @OnClick(R.id.btnCancel)
    public void cancel(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtLogin)
    public void login(View view) {
        // TODO submit data to server...
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_confirmation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mInteractor = new ConfirmationInteractorImpl();

    }


    @Override
    public void onSuccess() {
        startActivity(new Intent(this, ThanksActivity.class));
    }

    @Override
    public void onError() {

    }
}
