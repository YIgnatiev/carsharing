package youdrive.today.fragments;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.yandex.metrica.YandexMetrica;

import rx.Subscription;
import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.activities.MapsActivity;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterPaymentsBinding;
import youdrive.today.models.RegistrationUser;
import youdrive.today.models.User;
import youdrive.today.response.RegistrationModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterPaymentsFragment extends BaseFragment<RegistrationNewActivity> {

    private Subscription mSubscription;
    private FragmentRegisterPaymentsBinding b;
    private DialogFragment dialogFragment;
    private final String FINISH_URL = "https://youdrive.today/create-mob-account-done.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_payments, container, false);
        b.setListener(this);
        initWebView();
        return b.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_4_0");

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void updateUser(String userId, RegistrationUser user) {
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                .updateUser(userId, user)
                .subscribe(this::onUpdateSuccess, mActivity::onCreateFailure);
    }

    public void onUpdateSuccess(RegistrationModel model) {
        mActivity.hideProgress();
        String sessionId = model.getSession_id();
        if (sessionId != null) {
            User user = new User(sessionId, model.getData().getFirst_name(), null);
            if (App.getInstance().getPreference() != null) {
                App.getInstance().getPreference().putUser(new Gson().toJson(user));
            }

            YandexMetrica.reportEvent("registration_5_0");
            App.tracker().setScreenName("registration_5_0");
            App.tracker().send(new HitBuilders.ScreenViewBuilder().build());

            startActivity(new Intent(mActivity, MapsActivity.class));
            mActivity.finish();
        }
    }

    private void initWebView() {

        b.wvPayment.getSettings().setJavaScriptEnabled(true);
        b.wvPayment.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.v("Retrofit",url);
                if(url.equals(FINISH_URL)){
                    b.wvPayment.setVisibility(View.GONE);
                    updateUser(mActivity.userId,mActivity.mUser);
                }
            }
        });
    }

    private void openUrl(String url) {
        dialogFragment.dismiss();
        b.wvPayment.setVisibility(View.VISIBLE);
        b.wvPayment.loadUrl(url);
    }

    public void onCardLink(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialogFragment = PaymentDialogFragment.newInstance(mActivity.userId, getString(R.string.minimal_price), this::openUrl);
        dialogFragment.show(ft, "dialog");

    }

    public void onForvard(View view) {
        if (b.wvPayment.getVisibility() == View.VISIBLE) b.wvPayment.setVisibility(View.GONE);
        else updateUser(mActivity.userId, mActivity.mUser);
    }

    public void onBack(View view) {
        if (b.wvPayment.getVisibility() == View.VISIBLE) b.wvPayment.setVisibility(View.GONE);
        else mActivity.getFragmentManager().popBackStack();
    }
}
