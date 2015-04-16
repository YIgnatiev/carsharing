package youdrive.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class LoginActivity extends BaseActivity implements LoginActionListener {

    @InjectView(R.id.etLogin)
    EditText etLogin;

    @InjectView(R.id.etPassword)
    EditText etPassword;

    private LoginInteractorImpl mInteractor;

    @OnClick(R.id.btnLogin)
    public void submit(View view) {
        mInteractor.login(
                etLogin.getText().toString(),
                etPassword.getText().toString(),
                this);
    }

    @OnClick(R.id.txtRestore)
    public void restore(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        // TODO submit data to server...
    }

    @OnClick(R.id.txtRegistration)
    public void registration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mInteractor = new LoginInteractorImpl();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onError() {
        //TODO Error
    }
}
