package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;
import youdrive.today.helpers.AppUtils;
import youdrive.today.BaseActivity;
import youdrive.today.helpers.MaskedWatcher;
import youdrive.today.R;
import youdrive.today.models.Region;
import youdrive.today.databinding.ActivityRegistrationBinding;
import youdrive.today.listeners.RegistrationActionListener;
import youdrive.today.interceptors.RegistrationInteractorImpl;

public class RegistrationActivity extends BaseActivity implements RegistrationActionListener {

    private static final int DEFAULT_POSITION = 0;
    private static final int RC_CONFIRM = 1;
    private static final int RC_INFORM = 2;
    private static final int RC_THANKS = 3;


    private ActivityRegistrationBinding b;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        b.setListener(this);
        mInteractor = new RegistrationInteractorImpl();

        b.spRegion.setEnabled(false);
        mInteractor.getRegions(this);

        b.btnInvite.setIndeterminateProgressMode(true);

        mWatcher = new MaskedWatcher(b.etPhone, "# (###) ### ## ##");
    }

    private RegistrationInteractorImpl mInteractor;
    private List<Region> mRegions;

    private MaskedWatcher mWatcher;

    //listeners
    public void onOnvite(View view) {
        if (b.btnInvite.getProgress() == 0 && isValidate()) {

            setEnabledFields(false);
            b.btnInvite.setProgress(50);
            mInteractor.getInvite(
                    b.etLogin.getText().toString(),
                    Long.valueOf(mWatcher.getPhone()),
                    mRegions.get(b.spRegion.getSelectedItemPosition()).getId(),
                    true,
                    this);
        }
    }

    public void onLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onAbout(View view) {
        AppUtils.about(this);
    }


    private void setEnabledFields(boolean value) {
        b.etLogin.setEnabled(value);
        b.etPhone.setEnabled(value);
        b.spRegion.setEnabled(value);
        b.txtLogin.setEnabled(value);
        b.txtAbout.setEnabled(value);
    }


    private boolean isValidate() {
        boolean isLoginTyped = true;
        boolean isEmailValid = true;
        boolean isPhoneTyped = true;
        boolean isPhoneTooShort = true;
        boolean isSevenFirst = true;
        boolean areRegionsSelected = true;

        if (isEmpty(b.etLogin)) {
            b.etLogin.setError(getString(R.string.empty));
            isLoginTyped = false;
        } else if (!validateEmail(b.etLogin.getText().toString())) {
            b.etLogin.setError(getString(R.string.email_not_valid));
            isEmailValid = false;
        }

        if (isEmpty(b.etPhone)) {
            b.etPhone.setError(getString(R.string.empty));
            isPhoneTyped = false;
        } else if (b.etPhone.length() < 11) {
            b.etPhone.setError(getString(R.string.phone_not_valid));
            isPhoneTooShort = false;
        } else if (!mWatcher.getPhone().startsWith("7")) {
            b.etPhone.setError(getString(R.string.phone_format));
            isSevenFirst = false;
        }

        if (mRegions == null) {
            Toast.makeText(this, R.string.region_not_selected, Toast.LENGTH_LONG).show();
            areRegionsSelected = false;
        }

        return isLoginTyped && isEmailValid && isPhoneTyped && isPhoneTooShort &&isSevenFirst &&
                areRegionsSelected ;
    }



    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isEmpty(MaterialEditText et) {
        return et.getText().toString().trim().length() == 0;
    }


    @Override
    public void onInvite() {
        AppUtils.success(b.btnInvite, getString(R.string.open_car));
        if (b.spRegion.getSelectedItemPosition() < 1) {
            startActivityForResult(new Intent(this, ThanksActivity.class), RC_THANKS);
        } else {
            startActivityForResult(new Intent(this, InformActivity.class), RC_INFORM);
        }
    }

    @Override
    public void onRegions(List<Region> regions) {
        b.spRegion.setEnabled(true);
        mRegions = regions;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this,
                R.layout.item_spinner, getRegions(mRegions));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        b.spRegion.setAdapter(adapter);
        b.spRegion.setSelection(DEFAULT_POSITION);
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
        if (requestCode == RC_CONFIRM) {
            if (resultCode == RESULT_OK) {

            }

        } else if (requestCode == RC_INFORM || requestCode == RC_THANKS) {
            finish();
        }
    }

    @Override
    public void onError() {
        error(getString(R.string.internal_error));
        Timber.d("onError");
    }

    @Override
    public void onUnknownError(String text) {
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

    private void error(String text) {

        setEnabledFields(true);
        AppUtils.error(text, b.btnInvite);
    }
}
