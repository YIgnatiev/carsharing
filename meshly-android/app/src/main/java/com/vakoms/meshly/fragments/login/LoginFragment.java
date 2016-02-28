package com.vakoms.meshly.fragments.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vakoms.meshly.LoginActivity;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.FacebookUser;
import com.vakoms.meshly.models.Token;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.NetworkUtil;
import com.vakoms.meshly.utils.P;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.databinding.FragmentStarterLoginBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/22/15.
 */
public class LoginFragment extends BaseFragment<LoginActivity> implements FacebookCallback<LoginResult> {

    private FragmentStarterLoginBinding b;
    private FacebookUser mFacebookUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {

            b = FragmentStarterLoginBinding.inflate(inflater);
            b.setListener(this);
            initFacebook();
            startAnimation();
        }


        return b.getRoot();
    }

    private void startAnimation() {
        b.ivMeshlyLogo.animate().alpha(1).setInterpolator(new AccelerateInterpolator()).setDuration(2000).start();
        b.tvMeshlyMessage.animate().alpha(1).setInterpolator(new AccelerateInterpolator()).setDuration(2000).start();
        b.llDivider.animate().setInterpolator(new AccelerateInterpolator()).alpha(1).setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        b.llEmailContainer.setTranslationX(-2 * b.llEmailContainer.getWidth());
                        b.llFacebookContainer.setTranslationX(2 * b.llFacebookContainer.getWidth());
                        animateMovingFields();
                    }
                })
                .start();


    }

    private void animateMovingFields() {
        b.llFacebookContainer.setAlpha(1);
        b.llEmailContainer.setAlpha(1);
        b.llEmailContainer.animate().translationX(0).setInterpolator(new OvershootInterpolator()).setDuration(500).start();
        b.llFacebookContainer.animate().translationX(0).setInterpolator(new OvershootInterpolator()).setDuration(500).start();
    }



    //lisetners
    public void onEmailClicked(View view) {
        mActivity.replaceFragmentLeft(new LocalLoginFragment());
    }

    public void onFacebookClicked(View view) {
        loginWithFacebook();
    }

    public void onLinkedInClicked(View view) {
        if (NetworkUtil.isNetworkAvailable(mActivity)) {
            mActivity.initWebLogin();
        }
    }

    private void initFacebook() {

        FacebookSdk.sdkInitialize(mActivity);
        mActivity.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mActivity.callbackManager, this);

    }

    public void loginWithFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {

            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email", "user_friends"));
        } else {
            getFacebookUser(accessToken);
        }
    }



    private void getFacebookUser(AccessToken accessToken) {
        mFacebookUser = new FacebookUser();
        mFacebookUser.accessToken = accessToken.getToken();

        Subscription subscription = Observable
                .just(getFacebookRequest(accessToken))
                .timeout(3, TimeUnit.SECONDS)
                .map(graphRequest -> graphRequest.executeAndWait())
                .map(this::addFacebookData)
                .flatMap(facebookUser -> RetrofitApi.getInstance().meshly().loginWithFacebook(facebookUser))
                .flatMap(this::handleTokens)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoginSuccess, this::handleError);
        mSubscriptions.add(subscription);
    }

    public void onLoginSuccess(BaseResponse<UserMe> userMe) {
        mActivity.stopProgress();
        UserDAO dao = UserDAO.getInstance();
        dao.saveUserMe(mActivity.getContentResolver(), userMe.getData());
        mActivity.checkIfWellcomeScreenNeeded();
    }

    public void handleError(Throwable error) {
        mActivity.handleError(error);
    }

    private FacebookUser addFacebookData(GraphResponse response) {
        JSONObject object = response.getJSONObject();
        mFacebookUser.facebookId = getJsonString(object, "id");
        mFacebookUser.name = getJsonString(object, "name");
        mFacebookUser.email = getJsonString(object, "email");
        mFacebookUser.image = String.format(Constants.FACEBOOK_AVATAR_URL, mFacebookUser.getFacebookId());
        return mFacebookUser;
    }

    private Observable<BaseResponse<UserMe>> handleTokens(Token token) {
        if (token != null) {
            P.saveTokens(token.getLoginToken(), token.getRefreshToken(), token.getLoginExpiresIn(), token.getLoginTokenType());
            return RetrofitApi.getInstance().user().getRegisteredUserDetails();
        } else return Observable.error(new Exception());
    }

    private String getJsonString(JSONObject _object, String _key) {
        try {
            return _object.getString(_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GraphRequest getFacebookRequest(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, null);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        return request;
    }

    //facebook listener
    @Override
    public void onSuccess(LoginResult loginResult) {
        getFacebookUser(AccessToken.getCurrentAccessToken());
    }

    @Override
    public void onCancel() {
        Toast.makeText(mActivity, "Login canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException e) {
        Toast.makeText(mActivity, "Error occurred", Toast.LENGTH_SHORT).show();
    }
}
