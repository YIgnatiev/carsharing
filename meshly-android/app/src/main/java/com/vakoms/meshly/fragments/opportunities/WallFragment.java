package com.vakoms.meshly.fragments.opportunities;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.PeopleAdapter;
import com.vakoms.meshly.adapters.WallAdapter;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.chat.ChatFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.OnWallItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.User;
import com.vakoms.meshly.models.chat.ChatResponseModel;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.views.ResideMenu;
import com.vakoms.meshly.views.SwipeView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentWallBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */
public class WallFragment extends BaseFragment<MainActivity> implements OnWallItemListener,LoaderManager.LoaderCallbacks<Cursor> , ObservableScrollViewCallbacks{




    private User mUser;
    private WallModel mWalllmodel;

    private FragmentWallBinding b;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = FragmentWallBinding.inflate(inflater);
            b.setListener(this);
            setListView();
            getLoaderManager().restartLoader(1, null, this);
            addMenuListener(b.getRoot(),mActivity);

        }
        return b.getRoot();
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




    //listeners

    public void onMenuButton(View view){
        mActivity.openMainMenu();

    }

    public void onMyOpportunities(View view){
        startMyOpportunitiesFragment();

    }





    @Override
    public void onButtonClicked(WallModel model) {
        setUser(model.getUser());
        mWalllmodel = getModel(model);
        setInterested(mWalllmodel);
    }

    @Override
    public void onItemClicked(WallModel model) {
        startOpportunityFragment(model);
    }

    @Override
    public void onPictureClicked(String userId) {
        getUserById(userId);

    }





    private void startOpportunityFragment(WallModel _item) {
        OpportunitiesFragment fragment = OpportunitiesFragment.getInstance(_item);
        mActivity.replaceFragmentLeft(fragment);
    }

    private void startMyOpportunitiesFragment() {
        mActivity.replaceFragmentLeft(new MyOpportunitiesFragment());
    }

    private void setListView() {


      WallAdapter  adapter = new WallAdapter(R.drawable.fist_icon, getString(R.string.are_you_interested),this);

       b.rvOpportunities.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvOpportunities.setAdapter(adapter);
        b.rvOpportunities.setScrollViewCallbacks(this);
    }


    private void setInterested(WallModel _model) {

        mActivity.showProgress();
        Subscription interestedSubscription = RetrofitApi.getInstance()
                .wall()
                .interestedWallPost(_model)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(baseResponse -> RetrofitApi.getInstance().chat().getUserChats(mActivity.mUser.getId(), true))
                .map(BaseResponse::getData)
                .subscribe(response -> onChatSuccess(response, _model), this::handleError);


        mSubscriptions.add(interestedSubscription);
    }
    private void onChatSuccess(ChatResponseModel crm ,WallModel _model) {
        mActivity.stopProgress();
        String chatId = crm.findChatIdByUserId(mUser.getId());
        mActivity.replaceFragmentDown(ChatFragment.newInstance(chatId, mUser.getId(), getString(R.string.im_interested) + " \"" + _model.getTitle() + "\""));
    }









    private void getUserById(String _userId) {
        mActivity.showProgress();
        Subscription userSubscription = RetrofitApi.getInstance().user()
                .getUserWithExtraData(_userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(list-> list.get(0))
                .subscribe(this::onUserSuccess, this::handleError);


        mSubscriptions.add(userSubscription);
    }




    private void onUserSuccess(NewUser user) {
        mActivity.stopProgress();
        Fragment fragment = PeopleDetailFragment.getInstance(user.getId());
        mActivity.replaceFragmentLeft(fragment);

    }



    public  void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    private WallModel getModel(WallModel model) {

        ArrayList<String> listIds = new ArrayList<>();
        listIds.add(model.getId());
        model.setPosts(listIds);
        return model;

    }

    private void setUser(WallUser wallUser) {
        mUser = new User();
        mUser.setJob(wallUser.getJob());
        mUser.setId(wallUser.getId());
        mUser.setUsername(wallUser.getName());
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = MeshlyColumns.getColumns(WallModel.class);
        return new CursorLoader(mActivity, UserProvider.WALL_URI, columns, null, null, null);
    }



    boolean isLoadAllowed = true;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (isLoadAllowed) {
            isLoadAllowed = false;
            List<WallModel> list = ((WallAdapter) b.rvOpportunities.getAdapter()).getList();
            data.moveToFirst();
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                list.add(UserDAO.getInstance().getWallModel(data));
            }

            if(list.isEmpty())b.emptyView.setVisibility(View.VISIBLE);
            b.rvOpportunities.getAdapter().notifyDataSetChanged();
        }    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float flexibleRange = b.vwBackgroundToolbar.getHeight();
        float alpha = ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1);
        b.vwBackgroundToolbar.setAlpha(alpha);


    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

