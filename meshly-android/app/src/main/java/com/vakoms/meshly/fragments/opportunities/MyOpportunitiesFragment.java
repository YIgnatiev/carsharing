package com.vakoms.meshly.fragments.opportunities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.hidden.Recolor;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.PeopleAdapter;
import com.vakoms.meshly.adapters.WallAdapter;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.OnWallItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallModelMy;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.views.ResideMenu;
import com.vakoms.meshly.views.SwipeView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentMyOpportunitiesBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Oleh Makhobey on 20.05.2015.
 * tajcig@ya.ru
 */
public class MyOpportunitiesFragment extends BaseFragment<MainActivity> implements OnWallItemListener {

    private boolean isActive = true;
    private boolean isLoaded;
    private FragmentMyOpportunitiesBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            b = FragmentMyOpportunitiesBinding.inflate(inflater);
            b.setListener(this);
            initListViews();
            getActiveWallItems();
            b.rvExpired.setVisibility(View.GONE);


        return b.getRoot();
    }


    private void initListViews() {
        WallAdapter activeAdapter = new WallAdapter(R.drawable.delete, getString(R.string.delete), this);
        WallAdapter expiredAdapter = new WallAdapter(R.drawable.delete, getString(R.string.delete), this);


        b.rvActive.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvExpired.setLayoutManager(new LinearLayoutManager(mActivity));

        b.rvExpired.setAdapter(expiredAdapter);
        b.rvActive.setAdapter(activeAdapter);

    }

    private void getActiveWallItems() {
        mSubscriptions = new CompositeSubscription();

        mActivity.showProgress();

        Subscription subscription = RetrofitApi
                .getInstance()
                .wall()
                .getActiveWallPosts(0, 100)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onActiveWallSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }

    private void onActiveWallSuccess(List<WallModel> list) {
        mActivity.stopProgress();
        ((WallAdapter) b.rvActive.getAdapter()).getList().clear();
        ((WallAdapter) b.rvActive.getAdapter()).getList().addAll(list);
        b.rvActive.getAdapter().notifyDataSetChanged();

    }

    private void getExpiredWallItems() {
        mActivity.showProgress();

        Subscription subscription = RetrofitApi
                .getInstance()
                .wall()
                .getExpiredWallPosts(0, 100)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onExpiredWallSuccess, this::handleError);
        mSubscriptions.add(subscription);
    }

    private void onExpiredWallSuccess(List<WallModel> list) {
        mActivity.stopProgress();
        ((WallAdapter) b.rvExpired.getAdapter()).getList().addAll(list);
        b.rvExpired.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.getResideMenu().getSwipeDirections().clear();

    }


    private void switchToActive() {
        if (isActive) return;
        isActive = true;

        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Slide(Gravity.LEFT))
                .addTransition(new Fade());

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }



        TransitionManager.beginDelayedTransition((ViewGroup) b.getRoot(), set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);


        b.tvActive.setTextColor(Color.WHITE);
        b.tvExpired.setTextColor(getResources().getColor(R.color.white_transparent_50));
        b.rvActive.setVisibility(View.VISIBLE);
        b.rvExpired.setVisibility(View.GONE);


    }

    private void switchToExpired() {
        if (!isActive) return;
        isActive = false;

        if (!isLoaded) {
            isLoaded = true;
            getExpiredWallItems();
        }

        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Slide(Gravity.RIGHT))
                .addTransition(new Fade());

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }

        TransitionManager.beginDelayedTransition((ViewGroup) b.getRoot(), set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);
        b.tvActive.setTextColor(getResources().getColor(R.color.white_transparent_50));
        b.tvExpired.setTextColor(Color.WHITE);

        b.rvActive.setVisibility(View.GONE);
        b.rvExpired.setVisibility(View.VISIBLE);

    }

    private void startAddNewPostFragment() {
        mActivity.replaceFragmentLeft(new AddWallPostFragment());
    }

    private void deleteWallItem(WallModel _model) {

        mActivity.showProgress();


        Subscription deleteSubscription =
                RetrofitApi.getInstance()
                        .wall()
                        .deleteWallPost(_model.getId())
                        .timeout(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onDeleteSuccess, this::handleError);

        mSubscriptions.add(deleteSubscription);
    }

    private void onDeleteSuccess(BaseResponse response) {
        mActivity.stopProgress();
        getActiveWallItems();

    }

    private void getUserById(String _userId) {

        Subscription userSubscription = RetrofitApi.getInstance().user()
                .getUserWithExtraData(_userId)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(list -> list.get(0))
                .subscribe(this::onUserSuccess, this::handleError);

        mSubscriptions.add(userSubscription);
    }

    private void onUserSuccess(NewUser user) {
        mActivity.stopProgress();
        Fragment fragment = PeopleDetailFragment.getInstance(user.getId());
        mActivity.replaceFragmentDown(fragment);

    }

    public void handleError(Throwable throwable) {


        mActivity.handleError(throwable);


    }

    private void startEditWallFragment(WallModel _model) {
        mActivity.replaceFragmentLeft(AddWallPostFragment.getEditWallPostInstance(_model));
    }

    //listeners

    @Override
    public void onButtonClicked(WallModel model) {
        deleteWallItem(model);

    }

    @Override
    public void onItemClicked(WallModel model) {
        startEditWallFragment(model);

    }

    @Override
    public void onPictureClicked(String userId) {
        getUserById(userId);

    }

    public void onActive(View view) {
        switchToActive();
    }

    public void onExpired(View view) {
        switchToExpired();
    }

    public void onAddPost(View view) {
        startAddNewPostFragment();
    }

    public void onBack(View view) {
        mActivity.onBackPressed();

    }


}
