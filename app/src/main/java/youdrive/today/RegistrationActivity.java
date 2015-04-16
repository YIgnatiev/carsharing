package youdrive.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class RegistrationActivity extends BaseActivity implements RegistrationActionListener {

    private static final int DEFAULT_POSITION = 0;

    String[] mRegions = {"Москва", "Санкт-Петербург"};

    @InjectView(R.id.etLogin)
    EditText etLogin;

    @InjectView(R.id.etPhone)
    EditText etPhone;

    @InjectView(R.id.spRegion)
    Spinner spRegion;

    private RegistrationInteractorImpl mInteractor;

    @OnClick(R.id.btnInvite)
    public void invite(View view) {
        if (spRegion.getSelectedItemPosition() != DEFAULT_POSITION){
            startActivity(new Intent(this, ThanksActivity.class));
        } else {
            mInteractor.getRequest(
                    etLogin.getText().toString(),
                    etPhone.getText().toString(),
                    spRegion.getSelectedItem().toString(),
                    this);
        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mRegions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spRegion.setAdapter(adapter);
        spRegion.setSelection(DEFAULT_POSITION);
    }

    @Override
    public void onRequest(String request) {
        startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.REQUEST, request));
    }

    @Override
    public void onError() {

    }
}
