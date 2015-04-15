package youdrive.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class RegistrationActivity extends BaseActivity implements RegistrationActionListener {

    @InjectView(R.id.etLogin)
    EditText etLogin;

    @InjectView(R.id.etPhone)
    EditText etPhone;

    @InjectView(R.id.spRegion)
    Spinner spRegion;

    private RegistrationInteractorImpl mInteractor;

    @OnClick(R.id.btnInvite)
    public void invite(View view) {
        mInteractor.invite(
                etLogin.getText().toString(),
                etPhone.getText().toString(),
                "Moscow",
                this);
    }

    @OnClick(R.id.txtLogin)
    public void login(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        // TODO submit data to server...
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_registration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mInteractor = new RegistrationInteractorImpl();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
