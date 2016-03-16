package com.vakoms.meshly;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.vakoms.meshly.databases.DataBaseHelper;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.login.LoginFragment;
import com.vakoms.meshly.interfaces.VoidFunction;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.HasSeenWelcomeModel;
import com.vakoms.meshly.models.LinkedinData;
import com.vakoms.meshly.models.Token;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.P;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ActivityLoginBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private final int PHOTO_PERMISSION =11;

    private VoidFunction mPermissionListener;
    private Subscription mSubscription;
    public CallbackManager callbackManager;
    private ActivityLoginBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_login);
        replaceFragment(new LoginFragment());
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.getWritableDatabase();
    }

    @Override
    public void stopProgress() {
        b.clLoading.endProgress();
    }

    @Override
    public void showProgress() {
        b.clLoading.startProgress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribe(mSubscription);
    }

    private void unSubscribe(Subscription s) {
        if (s != null) s.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        int entryCount = getFragmentManager().getBackStackEntryCount();

        if (b.wvLinkedIn != null && b.wvLinkedIn.getVisibility() == View.VISIBLE) {
            b.wvLinkedIn.setVisibility(View.INVISIBLE);
            b.wvLinkedIn.loadUrl("about:blank");
        } else if (entryCount > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }






    public void showApp(HasSeenWelcomeModel<Boolean> model) {
        Intent intent ;
        if (!model.getHasSeenOnBoard()) intent = new Intent(this, WelcomeActivity.class);
        else intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getUserDetailsForReceivedTokens(Token token) {
        showProgress();
        mSubscription =
                handleTokens(token)
                        .map(userMe -> userMe.getData().getLinkedin().getToken())
                        .flatMap(linkedInToken -> RetrofitApi.getInstance().meshly().fetchLinkedInData(linkedInToken))
                        .timeout(3, TimeUnit.SECONDS)
                        .map(this::fromUserLinkedInToUserMe)
                        .flatMap(user -> RetrofitApi.getInstance().user().updateUser(user))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onLoginSuccess, this::handleError);

    }

    private Observable<BaseResponse<UserMe>> handleTokens(Token token) {
        if (token != null) {
            P.saveTokens(token.getLoginToken(), token.getRefreshToken(), token.getLoginExpiresIn(), token.getLoginTokenType());
            return RetrofitApi.getInstance().user().getRegisteredUserDetails();
        } else return Observable.error(new Exception());
    }

    private UserMe fromUserLinkedInToUserMe(LinkedinData userData) {
        UserMe user = new UserMe();

        user.linkedinId = userData.getId() != null ? userData.getId() : "";
        user.email = userData.getEmailAddress() != null ? userData.getEmailAddress() : "";
        user.username = userData.getFirstName() + " " + userData.getLastName();
        LinkedinData.PictureUrls pUrls = userData.getPictureUrls();
        if (pUrls != null && pUrls.getValues().size() > 0 && !pUrls.getValues().get(0).equals("")) {
            user.picture = pUrls.getValues().get(0);
        }
        user.summary = userData.getSummary();

        if (userData.getPositions() != null && userData.getPositions().getValues() != null) {
            if (userData.getPositions().getValues().size() > 0) {
                user.company = userData.getPositions().getValues().get(0).getCompany().getName();
                user.job = userData.getPositions().getValues().get(0).getTitle();
            }
        }
        if (userData.getPictureUrls() != null && userData.getPictureUrls().getValues().size() > 0) {
            user.picture = userData.getPictureUrls().getValues().get(0);
        }
        return user;
    }

    public void onLoginSuccess(UserMe userMe) {
        stopProgress();
        UserDAO dao = UserDAO.getInstance();
        dao.saveUserMe(getContentResolver(), userMe);
        checkIfWellcomeScreenNeeded();
    }




    public void checkIfWellcomeScreenNeeded(){
        mSubscription = RetrofitApi.getInstance().user().getHasWellcomeScreenNeeded()
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::showApp, this::handleError);
    }




    public void handleError(Throwable throwable) {
        stopProgress();

        String errorMessage = throwable.getMessage();
        if (errorMessage != null && errorMessage.contains("403"))
            Toast.makeText(this, "Wrong login or password", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initWebLogin() {
        //important, otherwise you'll not be unable to login via LinkedIn
        //after logout!
        CookieManager.getInstance().removeAllCookie();


        b.wvLinkedIn.getSettings().setJavaScriptEnabled(true);
        b.wvLinkedIn.addJavascriptInterface(new UserTokensParserJs(), "HtmlViewer");

        b.wvLinkedIn.setVisibility(View.VISIBLE);
        b.wvLinkedIn.requestFocus(View.FOCUS_DOWN);

        b.wvLinkedIn.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                stopProgress();

                if (url.startsWith(Constants.REDIRECT_URI)) {
                    b.wvLinkedIn.setVisibility(View.INVISIBLE);
                    b.wvLinkedIn.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //https://api.meshly.io/auth/linkedin/callback?error=access_denied&error_description=the+user+denied+your+request&state=***
                //cancel click
                if (authorizationUrl.contains("access_denied")) {
                    b.wvLinkedIn.stopLoading();
                    b.wvLinkedIn.setVisibility(View.INVISIBLE);
                    return false;
                }

                if (authorizationUrl.startsWith(Constants.REDIRECT_URI)) {

                    Uri uri = Uri.parse(authorizationUrl);
                    b.wvLinkedIn.loadUrl(uri.toString());
                } else {
                    b.wvLinkedIn.loadUrl(authorizationUrl);
                }

                return true;
            }
        });

        String authUrl = Constants.AUTHORIZATION_URL;

        b.wvLinkedIn.loadUrl(authUrl);
    }

    private class UserTokensParserJs {

        @SuppressWarnings("unused")
        @JavascriptInterface
        public void showHTML(String html) {

            if (!html.contains("access_token")) {
                return;
            }


            int begin = html.indexOf(">{") + 1;
            int end = html.indexOf("}<") + 1;
            String newJson = html.substring(begin, end);

            Token token = new Gson().fromJson(newJson, Token.class);
            runOnUiThread(() -> getUserDetailsForReceivedTokens(token));


        }
    }
















    public void checkForCameraPermission(VoidFunction function){
        int permissionCheck  = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");


        if (permissionCheck == PackageManager.PERMISSION_GRANTED)  function.apply();
        else if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            mPermissionListener = function;
            requstForPermission();
        }

    }




    private void requstForPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{"android.permission.CAMERA", "android.hardware.camera", "android.hardware.camera.autofocus"}, PHOTO_PERMISSION);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case  PHOTO_PERMISSION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                   mPermissionListener.apply();

                else
                    new AlertDialog.Builder(this, R.style.MyDialogTheme).setMessage("Meshly cannot take your photo").show();

                break;
            }

        }


    }




}