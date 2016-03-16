package com.vakoms.meshly;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.fragments.wellcome.ChooseIndustriesFragment;
import com.vakoms.meshly.fragments.wellcome.ChoosePopularPeopleFragment;
import com.vakoms.meshly.fragments.wellcome.WelcomeFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.HasSeenWelcomeModel;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.ReactiveLocation;

import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ActivityWellcomeBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    private ActivityWellcomeBinding b;
    private Subscription mSubscriptionSave;
    private Subscription mSubscriptionIsWelcomeSeen;
    private final int LOCATION_PERMISSION_REQUEST = 7;

    public UserMe mUser;
    static final int PAGE_COUNT =2;
    private boolean isFirst = true;
    public PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=   DataBindingUtil.setContentView(this, R.layout.activity_wellcome);
        b.setListener(this);
        setWelcomeAlreadySeen();
        checkForPermission();
        addFragment();
    }

    @Override
    protected void onStop() {
        if(mSubscriptionSave != null) mSubscriptionSave.unsubscribe();
        if(mSubscriptionIsWelcomeSeen != null) mSubscriptionIsWelcomeSeen.unsubscribe();
        super.onStop();
    }



    //listener
    public void onSkipClicked(View view){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onOk(View view){
        if(isFirst) {
            TransitionManager.beginDelayedTransition((ViewGroup)b.getRoot() ,new Fade());
            b.tvOk.setText("Next");
            b.llIndicator.setVisibility(View.VISIBLE);
            b.flContainer.setVisibility(View.GONE);
            b.viewPager.setVisibility(View.VISIBLE);
            b.vwPage1.setAlpha(1);
            b.vwPage2.setAlpha(.3f);
            isFirst = false;
        }else {


            if (b.viewPager.getCurrentItem() == 0)
                b.viewPager.setCurrentItem(1);
            else {
                saveSettings();
            }
        }


    }



    protected void checkForPermission(){

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED)  updateLocation();
        else if(permissionCheck == PackageManager.PERMISSION_DENIED)requstForPermission();

    }




    private void requstForPermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) updateLocation();

                else new AlertDialog.Builder(this, R.style.MyDialogTheme).setMessage("Meshly cannot receive your location now").show();

                break;
            }

        }
    }

    private void setWelcomeAlreadySeen(){

        HasSeenWelcomeModel<Integer> body = new HasSeenWelcomeModel<>();
        body.setHasSeenOnBoard(1);
        mSubscriptionIsWelcomeSeen = RetrofitApi
                .getInstance()
                .user()
                .saveHasSeen(body)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onSetWelcomeAlreadySeenSuccess, this::handleError);
    }

    private void onSetWelcomeAlreadySeenSuccess(HasSeenWelcomeModel<Boolean> response){

    }


    private void updateLocation() {


        Subscription locationSubscription =  ReactiveLocation.getInstance(this)

                .subscribeForLocationUpdates()
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleLocationSuccess, this::handleLocationFailure);

        subscriptions.add(locationSubscription);
    }




    private void handleLocationSuccess(Location location) {
        P.GPS.saveLocation(location);
    }

    private void handleLocationFailure(Throwable error) {

    }



    private void addFragment(){
        getFragmentManager().beginTransaction().add(R.id.flContainer,new WelcomeFragment() ).commit();
    }


    public  void initPager() {

        pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        b.viewPager.setAdapter(pagerAdapter);
        b.viewPager.addOnPageChangeListener(this);
        b.viewPager.setCurrentItem(0);

    }

    private void saveSettings(){

       showProgress();
        mSubscriptionSave = RetrofitApi
                .getInstance()
                .user()
                .updateUser(mUser)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSaveUserSuccess, this::handleError);




    }

    private void onSaveUserSuccess(UserMe user) {
        stopProgress();
        UserDAO.getInstance().saveUserMe(getContentResolver(), user);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showProgress() {
        b.pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgress() {
        b.pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void handleError(Throwable throwable) {
        stopProgress();
        Toast.makeText(this,"Network error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                TransitionManager.beginDelayedTransition(b.llIndicator, new Fade());
                b.vwPage1.setAlpha(1);
                b.vwPage2.setAlpha(.3f);
                b.tvOk.setText(getString(R.string.next));

                break;
            case 1:
                TransitionManager.beginDelayedTransition(b.llIndicator, new Fade());
                b.vwPage1.setAlpha(.3f);
                b.vwPage2.setAlpha(1);
                b.tvOk.setText(getString(R.string.lets_go));
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    return new ChoosePopularPeopleFragment();
                default:
                    return new ChooseIndustriesFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
