package youdrive.today.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.yandex.metrica.YandexMetrica;

import rx.Subscription;
import youdrive.today.App;
import youdrive.today.R;
import youdrive.today.activities.RegistrationActivity;
import youdrive.today.activities.WellcomeActivity;
import youdrive.today.databinding.FragmentRegisterOffertBinding;
import youdrive.today.helpers.PreferenceHelper;
import youdrive.today.response.RegistrationModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/25/15.
 */
public class RegisterOffertFragment extends BaseFragment<RegistrationActivity> implements CompoundButton.OnCheckedChangeListener {


    private FragmentRegisterOffertBinding b;
    private Subscription mSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_register_offert, container, false);
        b.setListener(this);
        setData();
        return b.getRoot();
    }

    private void setData() {
        initWebView();
        b.checkbox.setOnCheckedChangeListener(this);
        b.tvForvard.setEnabled(false);
    }

    private void initWebView() {

        b.webView.getSettings().setJavaScriptEnabled(true);
        b.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    b.webView.evaluateJavascript("javascript:window.document.getElementsByTagName('body')[0].style.color='white';", null);
                } else {
                    b.webView.loadUrl("javascript:window.document.getElementsByTagName('body')[0].style.color='white';");
                }
                b.webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(getActivity(), R.string.need_internet_for_registration, Toast.LENGTH_LONG).show();
                onBack(null);
            }
        });

        b.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        b.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        b.webView.setVisibility(View.INVISIBLE);
        b.webView.loadUrl(PreferenceHelper.EULA_URL);

        b.webView.setBackgroundColor(Color.TRANSPARENT);
    }


    public void onBack(View view) {
        startActivity(new Intent(mActivity, WellcomeActivity.class));
        mActivity.finish();
    }

    public void onForvard(View view) {
        createUser();

    }

    public void createUser() {
        mActivity.showProgress();
        mSubscription = mActivity.mClient
                .createUser()
                .subscribe(this::onCreateSucccess, mActivity::onCreateFailure);

    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("registration_1_0");
        App.tracker().setScreenName("registration_1_0");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onCreateSucccess(RegistrationModel model) {
        mActivity.hideProgress();
        mActivity.userId = model.getId();
        mActivity.mUser = model.getData();
        mActivity.startFragmentLeft(new RegisterProfileFragment());

    }

    @Override
    public void onStop() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        b.tvForvard.setEnabled(isChecked);
    }
}
