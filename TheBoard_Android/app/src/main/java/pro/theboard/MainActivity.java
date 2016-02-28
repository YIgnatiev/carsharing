package pro.theboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.ActivityMainBinding;

import java.util.Collections;
import java.util.List;

import pro.theboard.fragments.AboutFragment;
import pro.theboard.fragments.CardsFragment;
import pro.theboard.fragments.EmptyFragment;
import pro.theboard.fragments.SettingsFragment;
import pro.theboard.fragments.StarterFragment;
import pro.theboard.gcm.RegistrationIntentService;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;
import pro.theboard.rest.RetrofitApi;
import pro.theboard.utils.Preferences;
import rx.Subscription;

import static pro.theboard.constants.Constants.CARDS_FRAGMENT;
import static pro.theboard.constants.Constants.REGISTRATION_COMPLETE;
import static pro.theboard.constants.Constants.SENT_TOKEN_TO_SERVER;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 30/03/15.
 */


public class MainActivity extends BaseActivity {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Subscription mQuestionsSubcription;
    private Subscription mPromoSubscription;

    private long lastTimeBackPressed;


    private TextView tvActionBarTitle;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private ProgressDialog pbLoading;

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        pbLoading = new ProgressDialog(this);
        pbLoading.setMessage(getString(R.string.loading));
        setMenuLayout();
        b.setListener(this);

        setToolBar();
        replaceFragment(new StarterFragment());
        initGcm();
    }


    @Override
    protected void onStop() {
        if (mQuestionsSubcription != null) mQuestionsSubcription.unsubscribe();
        if (mPromoSubscription != null) mPromoSubscription.unsubscribe();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (lastTimeBackPressed + 3000 > System.currentTimeMillis()) {
            finish();
        } else {
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this, R.string.press_once_again, Toast.LENGTH_SHORT).show();
        }

    }


    private void setMenuLayout() {

        b.include.getRoot().setTranslationX(-getScreenWidth() / 3);

    }

    private void setToolBar() {

        View view = getLayoutInflater().inflate(R.layout.item_toolbar, null, false);


        view.findViewById(R.id.iv_menu_toolbar).setOnClickListener(this::onMenu);
        tvActionBarTitle = (TextView) view.findViewById(R.id.tvActionBarTitle);
        b.toolbar.addView(view);
    }

    public void setToolbarTitle(String _title) {
        tvActionBarTitle.setText(_title);
    }


    private void hideToolbar() {
        // ivActionBarLogo.setVisibility(View.GONE);
    }


    private void startCardsFragment(List<Model> _list, boolean isPromo) {

        Fragment fragment = CardsFragment.getQuestionsInstance(_list, isPromo);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment, CARDS_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    private int getScreenWidth() {
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void replaceFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }

    private void openMenu() {
        b.include.getRoot().animate()
                .translationX(0)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        b.include.getRoot().setVisibility(View.VISIBLE);
                    }
                });

        b.rlContent.animate()
                .translationX(getScreenWidth())
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator());
    }

    private void closeMenu() {

        b.include.getRoot().animate()
                .translationX(-getScreenWidth() / 3)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        b.include.getRoot().setVisibility(View.GONE);
                    }
                });

        b.rlContent
                .animate()
                .translationX(0)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator());
    }

    private void showAboutPage() {
        closeMenu();
        replaceFragment(new AboutFragment());
    }

    private void showSettingsPage() {
        closeMenu();
        replaceFragment(new SettingsFragment());
    }

//listeners

    public void onArrow(View view) {
        closeMenu();
    }

    public void onCards(View view) {
        if (!Preferences.getIsAdult()) closeMenu();
        getQuestions();
    }

    public void onSettings(View view) {
        showSettingsPage();
    }

    public void onPromo(View view) {
        getAnsweredQuestions();
    }

    public void onAbout(View view) {
        showAboutPage();
    }

    public void onMenu(View view) {
        openMenu();
    }


//gcm methods

    private void initGcm() {

        initBroadcast();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            this.startService(intent);
        }
    }


    private void initBroadcast() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getBoolean(SENT_TOKEN_TO_SERVER, false);

            }
        };
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(REGISTRATION_COMPLETE));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil
                        .getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }

// gcm methods


    @Override
    public void getQuestions() {
        pbLoading.show();
        mQuestionsSubcription = RetrofitApi
                .getInstance(this)
                .getCards()
                .subscribe(this::onCardReceived, this::onFailure);

    }


    private void onCardReceived(List<Model> models) {
        //addFakeCard(models);
        Collections.sort(models, (lhs, rhs) -> rhs.getOrder() - lhs.getOrder());

        pbLoading.dismiss();
        hideToolbar();
        startCardsFragment(models, false);
        closeMenu();

    }


    @Override
    public void sendAnswer(Answer _answer) {

        RetrofitApi
                .getInstance(this)
                .sendAnswer(_answer)
                .subscribe(this::onAnswerSuccess, this::onFailure);
    }

    private void onAnswerSuccess(List dummyList) {

    }

    private void onFailure(Throwable error) {
        pbLoading.dismiss();
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
    }


    public void getAnsweredQuestions() {
        pbLoading.show();
        mPromoSubscription = RetrofitApi.getInstance(this)
                .getPromoCards()
                .subscribe(this::showPromoCards, this::onFailure);
        //Realm realm = Realm.getInstance(this);


        //RealmResults<RealmModel> query = realm.where(RealmModel.class).findAll();
        //if(query != null) {
        //  List<Model> promoCards = new ArrayList<>();
        //for (RealmModel model : query) {
        //    promoCards.add(ModelAdapter.toModel(model));
        // }


        //}

        closeMenu();


    }


    private void showPromoCards(List<Model> promoCards) {
        pbLoading.hide();

        if (promoCards.isEmpty()) replaceFragment(new EmptyFragment());
        else startCardsFragment(promoCards, true);

    }


}
