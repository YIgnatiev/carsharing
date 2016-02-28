package com.vakoms.meshly.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.views.CustomSwitch;

import java.util.ArrayList;
import java.util.Arrays;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey on 23.05.2015.
 * tajcig@ya.ru
 */
public class ShareFragment extends Fragment implements View.OnClickListener,
        CustomSwitch.OnCheckedChangeListener, OnLoginCompleteListener , OnPostingCompleteListener , FacebookCallback<LoginResult>{


    private String TWITTER_CONSUMER_KEY;
    private String TWITTER_CONSUMER_SECRET;


    public static final String LINK = "http://meshly.io/";

    private SocialNetworkManager mSocialNetworkManager;

    private MainActivity mActivity;
    private LinearLayout llBack;
    private TextView tvShare;
    private EditText etMessage;
    private CustomSwitch csLinkedIn;
    private CustomSwitch csTwitter;
    private CustomSwitch csFacebook;
    private WallModel mWallModel;
    private CallbackManager callbackManager;
    private ArrayList<Integer> mSocialNetworksStack;
    private final int FACEBOOK = 777;
    public static ShareFragment getInstance(WallModel _model) {
        ShareFragment fragment = new ShareFragment();
        fragment.setWallModel(_model);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = (MainActivity) activity;

        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall_share, container, false);

        findViews(rootView);
        setListeners();
        setData();
        initSocialNetworks();
        initFacebook();
        return rootView;
    }


    public void setWallModel(WallModel _model) {
        this.mWallModel = _model;
    }




    private void setData(){
        etMessage.setText(mWallModel.getTitle());

        mSocialNetworksStack = new ArrayList<>();
        TWITTER_CONSUMER_KEY = getString(R.string.twitter_consumer_key);
        TWITTER_CONSUMER_SECRET = getString(R.string.twitter_consumer_secret);
    }


    private void findViews(View _rootView) {
        llBack = (LinearLayout) _rootView.findViewById(R.id.llBack_FragmentWallShare);
        tvShare = (TextView) _rootView.findViewById(R.id.tvShare_FragmentWallShare);
        csLinkedIn = (CustomSwitch) _rootView.findViewById(R.id.csLinkedIn_FragmentWallShare);
        csTwitter = (CustomSwitch) _rootView.findViewById(R.id.csTwitter_FragmentWallShare);
        csFacebook = (CustomSwitch) _rootView.findViewById(R.id.csFacebook_FragmentWallShare);
        etMessage = (EditText) _rootView.findViewById(R.id.etMessage_FragmentWallShare);
    }

    private void setListeners() {
        llBack.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        csLinkedIn.setOnCheckedChangeListener(this);
        csFacebook.setOnCheckedChangeListener(this);
        csTwitter.setOnCheckedChangeListener(this);

    }





    private void sendPost(int _socialNetwork){
       makePostLink(_socialNetwork, etMessage.getText().toString(), this);
    }

    private void shareOnSocialNetwork() {
        if (!mSocialNetworksStack.isEmpty()) {


            mActivity.showProgress();
            if(mSocialNetworksStack.contains(TwitterSocialNetwork.ID)){
                sendPost(TwitterSocialNetwork.ID);
                mSocialNetworksStack.remove(new Integer(TwitterSocialNetwork.ID));
                return;
            }
            if(mSocialNetworksStack.contains(FACEBOOK)){
                sendPost(FACEBOOK);
                mSocialNetworksStack.remove(new Integer(FACEBOOK));

            }


        } else {
            KeyboardUtil.hideKeyBoard(etMessage, mActivity);
        mActivity.onBackPressed();
        }
    }

    private void checkNetwork(int _socialNetwork , boolean _isChecked) {

        if(_isChecked && !mSocialNetworksStack.contains(_socialNetwork))mSocialNetworksStack.add(_socialNetwork);
        if(!_isChecked && mSocialNetworksStack.contains(_socialNetwork))mSocialNetworksStack.remove(new Integer(_socialNetwork));



        if(_socialNetwork == FACEBOOK){
            loginWithFacebook();
            return;
        }

        if(!checkLogin(_socialNetwork)){
            requestLogin(_socialNetwork, this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack_FragmentWallShare:
                KeyboardUtil.hideKeyBoard(etMessage, mActivity);
                mActivity.onBackPressed();
                break;
            case R.id.tvShare_FragmentWallShare:
                shareOnSocialNetwork();
                break;

        }
    }

    @Override
    public void onStateChanged(View view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.csFacebook_FragmentWallShare:


                checkNetwork(FACEBOOK, isChecked);
                break;
            case R.id.csTwitter_FragmentWallShare:

                 checkNetwork(TwitterSocialNetwork.ID, isChecked);
                break;
        }
    }






    @Override
    public void onLoginSuccess(int i) {

    }

    @Override
    public void onError(int i, String s, String s1, Object o) {
        mActivity.stopProgress();
    }

    @Override
    public void onPostSuccessfully(int i) {
        shareOnSocialNetwork();
        mActivity.stopProgress();
    }








    private void initFacebook() {

        FacebookSdk.sdkInitialize(mActivity.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);

    }

    private void shareOnFacbook() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://meshly.io/#opportunities/" + mWallModel.getId()))
                .setContentTitle(mWallModel.getTitle())
                .setContentDescription(etMessage.getText().toString())
                .build();

        ShareDialog.show(mActivity, content);
    }


        public void initSocialNetworks() {




//TODO implement social network;
//            mSocialNetworkManager = (SocialNetworkManager) getFragmentManager()
//                    .findFragmentByTag(MainActivity.SOCIAL_NETWORK_TAG);

            //Check if manager exist
            if (mSocialNetworkManager == null) {
                mSocialNetworkManager = new SocialNetworkManager();



//                SocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY,TWITTER_CONSUMER_SECRET, "oauth://ASNE");

//            SocialNetwork linkedInSocialNetwork = new LinkedInSocialNetwork(mFragment,LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET, "http://www.hotel-face.com", linkedInScope);


//                mSocialNetworkManager.addSocialNetwork(twNetwork);



                //Initiate every network from mSocialNetworkManager
               // getFragmentManager().beginTransaction().add(mSocialNetworkManager, MainActivity.SOCIAL_NETWORK_TAG).commit();


            }

        }



        public void requestLogin(int networkId, OnLoginCompleteListener callback) {




            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
            socialNetwork.requestLogin(callback);
        }

        public boolean checkLogin(int networkId) {


            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);

            return socialNetwork.isConnected();
        }

        public void makePostLink(int mNetworkId,String message,OnPostingCompleteListener callback) {


            if(mNetworkId == FACEBOOK){
                shareOnFacbook();
                return;
            }

            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(mNetworkId);
            Bundle postParams = new Bundle();

            postParams.putString(SocialNetwork.BUNDLE_LINK, LINK);
            postParams.putString(SocialNetwork.BUNDLE_MESSAGE, message);


            socialNetwork.requestPostLink(postParams, message, callback);
        }



    private void loginWithFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken == null) {

            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email", "user_friends"));
        }
    }



        private String formatForTwitter(String message) {
            int lastSpacePosition = 0;
            if (message.length() < 140) {
                return message;
            }
            for (int i = 137; i > 0; i--) {

                if (message.charAt(i) == ' ') {
                    lastSpacePosition = i;
                    break;
                }
            }
            message = message.substring(0, lastSpacePosition).concat("...");
            return message;
        }


    @Override
    public void onSuccess(LoginResult loginResult) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException e) {

    }
}





















//
//
//
//    private void shareOnTwitter() {
//        try {
//            TweetComposer.Builder builder = new TweetComposer.Builder(mActivity)
//                    .text(etMessage.getText().toString())
//                    .url(new URL("http://meshly.io/"));
//
//            builder.show();
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }
//    }