package pro.theboard;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.ActivityLoginBinding;

import pro.theboard.fragments.K18CardFragment;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.retrofit.Customer;
import pro.theboard.rest.RetrofitApi;
import pro.theboard.utils.NetworkUtil;
import pro.theboard.utils.Preferences;
import rx.Subscription;

import static pro.theboard.constants.Constants.CARDS_FRAGMENT;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 30/03/15.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AlertDialog mDialog;
    private ActivityLoginBinding b;
    private TextView tvTitle;
    private Subscription mLoginSubscription;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setToolBar();

        new Operations(this).login();
    }


    private void setToolBar() {

        View toolbarView = getLayoutInflater().inflate(R.layout.item_toolbar, null);
        toolbarView.findViewById(R.id.iv_menu_toolbar).setVisibility(View.GONE);
        tvTitle = (TextView) toolbarView.findViewById(R.id.tvActionBarTitle);
        b.toolbar.addView(toolbarView);
    }

    public void setToolbarTitle(String _title) {
        tvTitle.setText(_title);
    }

    @Override
    public void getQuestions() {
    }

    @Override
    public void sendAnswer(Answer _answer) {
    }


    public void requestLogin() {

        if (NetworkUtil.isNetworkAvailable(this)) {
            startLoading();
            buildGoogleApiClient();
        } else {
            showNetworkDialog();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener((result) -> onLocationFailed(null))
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) onLocationSuccess(location);
        else onLocationFailed(null);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        onLocationFailed(null);
    }

    private void onLocationSuccess(Location location) {
        Log.v("login", "location success");
        String latitude = String.valueOf(location.getLatitude());
        String logitude = String.valueOf(location.getLongitude());

        RetrofitApi.getInstance(this)
                .login(latitude, logitude, true, true)
                .subscribe(this::onLoginSuccess, this::onLoginFailure);
    }

    private void onLocationFailed(Throwable throwable) {

        mLoginSubscription = RetrofitApi.getInstance(this)
                .login("0", "0", true, true)
                .subscribe(this::onLoginSuccess, this::onLoginFailure);

    }

    private void onLoginSuccess(Customer responseCustomer) {
        stopLoading();
        Preferences.saveCustomerHash(responseCustomer.getCustomer_hash());
        startMainActivity();
    }

    private void onLoginFailure(Throwable error) {
        stopLoading();
        showNetworkDialog();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startAlertFragment() {

        Fragment fragment = new K18CardFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment, CARDS_FRAGMENT)
                .commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvTryAgain_DialogNetwork:
                mDialog.dismiss();
                requestLogin();
                break;
            case R.id.tvExit_DialogNetwork:
                mDialog.dismiss();
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStop() {
        if (mLoginSubscription != null) mLoginSubscription.unsubscribe();
        super.onStop();
    }

    private void showNetworkDialog() {

        View rootView = getLayoutInflater().inflate(R.layout.dialog_network, null);
        rootView.findViewById(R.id.tvTryAgain_DialogNetwork).setOnClickListener(this);
        rootView.findViewById(R.id.tvExit_DialogNetwork).setOnClickListener(this);
        mDialog = new AlertDialog.Builder(this)
                .setView(rootView)
                .setCancelable(false)
                .create();
        mDialog.show();
    }


    private void startLoading() {
        b.pbLoading.setVisibility(View.VISIBLE);
        b.vwLoadingBackground.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        b.pbLoading.setVisibility(View.GONE);
        b.vwLoadingBackground.setVisibility(View.GONE);
    }


}
