package youdrive.today.fragments;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import rx.Subscription;
import youdrive.today.R;
import youdrive.today.activities.MapsActivity;
import youdrive.today.activities.RegistrationNewActivity;
import youdrive.today.databinding.FragmentRegisterPaymentsBinding;
import youdrive.today.models.RegistrationUser;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_payments, container, false);
        b.setListener(this);
        setData();
        initWebView();
        return b.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    private void setData() {
        String paymentsText = getString(R.string.payments, mActivity.price);
    }


    public void onBack(View view) {

        if (b.wvPayment.getVisibility() == View.VISIBLE) b.wvPayment.setVisibility(View.GONE);
        else mActivity.getFragmentManager().popBackStack();
    }

    public void onForvard(View view) {
        if (b.wvPayment.getVisibility() == View.VISIBLE) b.wvPayment.setVisibility(View.GONE);
        else updateUser(mActivity.userId, mActivity.mUser);
    }

    public void updateUser(String userId, RegistrationUser user) {
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                .updateUser(userId, user)
                .subscribe(this::onUpdateSuccess, mActivity::onCreateFailure);
    }

    public void onUpdateSuccess(RegistrationModel model) {
        mActivity.hideProgress();
        startActivity(new Intent(mActivity, MapsActivity.class));
        mActivity.finish();
    }

    public void onCardLink(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        dialogFragment = PaymentDialogFragment.newInstance(mActivity.userId, mActivity.price, this::openUrl);
        dialogFragment.show(ft, "dialog");

    }


    private void initWebView() {
        b.wvPayment.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    private void openUrl(String url) {
        dialogFragment.dismiss();
        b.wvPayment.setVisibility(View.VISIBLE);
        b.wvPayment.loadUrl(url);
    }

}
