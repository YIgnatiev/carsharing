package youdrive.today.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.Region;
import youdrive.today.login.RegistrationActionListener;
import youdrive.today.login.impl.RegistrationInteractorImpl;

/**
 * Created by psuhoterin on 15.04.15.
 */
public class RegistrationActivity extends BaseActivity implements RegistrationActionListener {

    private static final int DEFAULT_POSITION = 0;

    Handler mHandler = new Handler();

    @InjectView(R.id.etLogin)
    EditText etLogin;

    @InjectView(R.id.etPhone)
    EditText etPhone;

    @InjectView(R.id.spRegion)
    Spinner spRegion;

    private RegistrationInteractorImpl mInteractor;
    private List<Region> mRegions;

    @OnClick(R.id.btnInvite)
    public void invite(View view) {
//        mInteractor.getRequest(
//                    etLogin.getText().toString(),
//                    etPhone.getText().toString(),
//                    mRegions.get(spRegion.getSelectedItemPosition()).getId(),
//                    this);
        startActivity(new Intent(this, ConfirmationActivity.class));
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
        mInteractor.getRegions(this);
    }

    @Override
    public void onRequest() {
            startActivity(new Intent(this, ThanksActivity.class));
    }

    @Override
    public void onRegions(List<Region> regions) {
        Timber.d("Regions " + regions.toString());
        mRegions = regions;

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this,
                        R.layout.item_spinner, getRegions(mRegions));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spRegion.setAdapter(adapter);
                spRegion.setSelection(DEFAULT_POSITION);
            }
        });

    }

    private List<String> getRegions(List<Region> regions){
        List<String> array = new ArrayList<>();
        for (Region r: regions){
            array.add(r.getName());
        }
        return array;
    }

    @Override
    public void onError() {
        Timber.d("onError");
    }

    @Override
    public void onUnknownError() {
        Timber.d("onUnknownError");
    }

    @Override
    public void onRegionNotFound() {
        Timber.d("onRegionNotFound");
    }

    @Override
    public void onUserAlreadyExist() {
        Timber.d("onUserAlreadyExist");
    }
}
