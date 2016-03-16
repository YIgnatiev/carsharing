package com.vakoms.meshly.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.vakoms.meshly.constants.Constants;

import meshly.vakoms.com.meshly.R;

/**
 * Created by lubomyr.ivanitskiy on 1/2/15.
 */
public class PrivacyPolicyInfoFragment extends Fragment implements View.OnClickListener {
    private WebView wvInfo;
    private ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_privacy_policy, container,false);
        findViews(rootView);
        setListeners();
        setupWebView();
        return rootView;
    }

    private void findViews(View _rootView) {
        ivBack = (ImageView) _rootView.findViewById(R.id.ivBack);
        wvInfo = ((WebView) _rootView.findViewById(R.id.wvInfo_FragmentPrivacyPolicy));
    }

    private void setListeners() {
        ivBack.setOnClickListener(this);
    }

    private void setupWebView() {
        wvInfo.getSettings().setJavaScriptEnabled(true);
        wvInfo.loadUrl(Constants.PRIVACY_POLICY_URL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
        }
    }
}
