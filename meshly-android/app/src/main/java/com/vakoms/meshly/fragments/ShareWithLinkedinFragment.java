package com.vakoms.meshly.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.models.LinkedInMassage;
import com.vakoms.meshly.models.LinkedInResponse;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ShareFragmentb;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by taras.melko on 15.09.2014.
 */
public class ShareWithLinkedinFragment extends BaseFragment {

    private static final String NAME = "name";
    private static final String TEXT = "text";
    private static final String PROFILE = "profile";

    private MainActivity mActivity;

    private ShareFragmentb b;


    public static ShareWithLinkedinFragment instance(String name, String text, boolean isProfile) {
        ShareWithLinkedinFragment frag = new ShareWithLinkedinFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, name);
        bundle.putString(TEXT, text);
        bundle.putBoolean(PROFILE, isProfile);
        frag.setArguments(bundle);
        return frag;
    }


    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideKeyBoard(b.etShareText, mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mActivity = (MainActivity) getActivity();

        b = DataBindingUtil.inflate(inflater, R.layout.fragment_share, null, false);
        b.setListener(this);
        setupView();
        return b.getRoot();
    }

    private void setupView() {

        b.tvName.setText(getArguments().getString(NAME));
        b.etShareText.setText(getArguments().getString(TEXT));
    }


    public void onBackClicked(View view) {
        mActivity.onBackPressed();

    }

    public void onOkclicked(View view) {
        share();

    }


    private void share() {
        mActivity.showProgress();


        String token = mActivity.mUser.linkedin.token;

        String message = b.etShareText.getText().toString();
        LinkedInMassage messageObj = new LinkedInMassage();
        messageObj.setComment(message);


        Subscription subscription = RetrofitApi
                .getInstance()
                .meshly()
                .shareOnLinkedIn(token, messageObj)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onShareSuccess, this::handleError);


        mSubscriptions.add(subscription);

    }

    private void onShareSuccess(LinkedInResponse response) {
        mActivity.stopProgress();
        Toast.makeText(getActivity(), "Thanks!", Toast.LENGTH_SHORT).show();
        mActivity.onBackPressed();
    }

    public void handleError(Throwable t) {
mActivity.handleError(t);
    }


}
