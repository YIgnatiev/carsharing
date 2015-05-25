package youdrive.today.login.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import timber.log.Timber;
import youdrive.today.AppUtils;
import youdrive.today.BaseActivity;
import youdrive.today.MaskedWatcher;
import youdrive.today.R;
import youdrive.today.Region;
import youdrive.today.login.RegistrationActionListener;
import youdrive.today.login.impl.RegistrationInteractorImpl;

public class RegistrationActivity extends BaseActivity implements RegistrationActionListener {

    private static final int DEFAULT_POSITION = 0;
    private static final int RC_CONFIRM = 1;
    private static final int RC_INFORM = 2;
    private static final int RC_THANKS = 3;

    @InjectView(R.id.etLogin)
    MaterialEditText etLogin;

    @InjectView(R.id.etPhone)
    MaterialEditText etPhone;

    @InjectView(R.id.spRegion)
    Spinner spRegion;

    @InjectViews({R.id.etLogin, R.id.etPhone, R.id.spRegion, R.id.txtLogin, R.id.txtAbout})
    List<View> vInputs;

    @InjectView(R.id.btnInvite)
    CircularProgressButton btnInvite;

    private RegistrationInteractorImpl mInteractor;
    private List<Region> mRegions;

    @OnClick(R.id.btnInvite)
    public void invite() {
        if (btnInvite.getProgress()==0
                && isValidate()) {
            startActivityForResult(new Intent(this, ConfirmationActivity.class), RC_CONFIRM);
        }
    }

    @OnClick(R.id.txtLogin)
    public void login(View view) {
        finish();
    }

    @OnClick(R.id.txtAbout)
    public void about(View view) {
        AppUtils.about(this);
    }

    private boolean isValidate() {
        boolean isValidate = true;

        if (isEmpty(etLogin)) {
            etLogin.setError(getString(R.string.empty));
            isValidate = false;
        } else if (!etLogin.getText().toString().contains("@")) {
            etLogin.setError(getString(R.string.email_not_valid));
            isValidate = false;
        }

        if (isEmpty(etPhone)) {
            etPhone.setError(getString(R.string.empty));
            isValidate = false;
        }

        return isValidate;
    }

    private boolean isEmpty(MaterialEditText et) {
        return et.getText().toString().trim().length() == 0;
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

        spRegion.setEnabled(false);
        mInteractor.getRegions(this);

        btnInvite.setIndeterminateProgressMode(true);

        new MaskedWatcher(etPhone, "# (###) ### ## ##");
    }

    @Override
    public void onInvite() {
        AppUtils.success(btnInvite, getString(R.string.open_car));
        if (spRegion.getSelectedItemPosition() < 1) {
            startActivityForResult(new Intent(this, ThanksActivity.class), RC_THANKS);
        } else {
            startActivityForResult(new Intent(this, InformActivity.class), RC_INFORM);
        }
    }

    @Override
    public void onRegions(List<Region> regions) {
        spRegion.setEnabled(true);
        mRegions = regions;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this,
                R.layout.item_spinner, getRegions(mRegions));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spRegion.setAdapter(adapter);
        spRegion.setSelection(DEFAULT_POSITION);
    }

    private List<String> getRegions(List<Region> regions) {
        List<String> array = new ArrayList<>();
        for (Region r : regions) {
            array.add(r.getName());
        }
        return array;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CONFIRM
                && resultCode == RESULT_OK) {

            if (mRegions != null){
                ButterKnife.apply(vInputs, AppUtils.ENABLED, false);
                btnInvite.setProgress(50);
                mInteractor.getInvite(
                        etLogin.getText().toString(),
                        etPhone.getText().toString(),
                        mRegions.get(spRegion.getSelectedItemPosition()).getId(),
                        this);
            } else {
                Toast.makeText(this, R.string.region_not_selected, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == RC_INFORM
                || requestCode == RC_THANKS) {
            finish();
        }
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
    public void onRegionNotFound(String text) {
        error(text);
        Timber.d("onRegionNotFound");
    }

    @Override
    public void onUserAlreadyExist(String text) {
        error(text);
        Timber.d("onUserAlreadyExist");
    }

    @Override
    protected void onDestroy() {
        mInteractor.getSubscription().unsubscribe();
        super.onDestroy();
    }

    private void error(String text){
        ButterKnife.apply(vInputs, AppUtils.ENABLED, true);
        AppUtils.error(text,
                btnInvite);
    }
}
