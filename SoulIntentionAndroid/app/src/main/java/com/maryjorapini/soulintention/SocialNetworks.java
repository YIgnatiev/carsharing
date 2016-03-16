package com.maryjorapini.soulintention;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;

import com.maryjorapini.soulintention.adapters.PostAdapter;
import com.maryjorapini.soulintention.global.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Администратор on 25.11.2014.
 */


public class SocialNetworks {

    private String                    TWITTER_CONSUMER_KEY;
    private String                    TWITTER_CONSUMER_SECRET;
    private Fragment                  mFragment;
    private SocialNetworkManager      mSocialNetworkManager;
    public SocialNetworks(Fragment fragment){
        mFragment = fragment;
        TWITTER_CONSUMER_KEY = fragment.getString(R.string.twitter_consumer_key);
        TWITTER_CONSUMER_SECRET = fragment.getString(R.string.twitter_cosumer_secret);



//        TWITTER_CONSUMER_KEY = "hkrKqr42XyGffiJYAYixmIS8x";
//        TWITTER_CONSUMER_SECRET ="5HigFA5tTFMBRXAxRxvLbpSfzWy4Swzo4d3qgpvd113DxXN2sj";
        initSocialNetworks();
    }

    public void initSocialNetworks(){
        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile", "email", "user_friends"));

        mSocialNetworkManager = (SocialNetworkManager) mFragment.getFragmentManager().findFragmentByTag(Constants.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(mFragment, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(mFragment, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, "https://github.com/gorbin/ASNE");
            mSocialNetworkManager.addSocialNetwork(twNetwork);

             //Initiate every network from mSocialNetworkManager
            mFragment.getFragmentManager().beginTransaction().add(mSocialNetworkManager, Constants.SOCIAL_NETWORK_TAG).commit();

        }

    }

    public boolean checkForLogin(int networkId) {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        return socialNetwork.isConnected();
    }

    public void requestLogin(int _networkId ,  OnLoginCompleteListener callback){
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(_networkId);
        socialNetwork.requestLogin(callback);
    }




    public  void makeRepost(int networkId, String message , OnPostingCompleteListener listener) {

       if(networkId == TwitterSocialNetwork.ID) {
                message = formatForTwitter(message);
        }
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestPostMessage(message,listener);
    }

    private String formatForTwitter(String message){
        int lastSpacePosition = 0;
        if(message.length() < 140){
            return message;
        }
        for (int i = 137; i > 0 ; i--){

            if (message.charAt(i) == ' '){
                lastSpacePosition = i;
                break;
            }
        }
        message = message.substring(0, lastSpacePosition).concat("...");
        return message;
    }




}
