package com.vakoms.meshly.fragments.opportunities;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.ShareFragment;
import com.vakoms.meshly.fragments.chat.ChatFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.chat.ChatResponseModel;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.models.wall.WallUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.views.LabelLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentOpportunitiesBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 29.04.2015.
 * tajcig@ya.ru
 */
public class OpportunitiesFragment extends BaseFragment<MainActivity> {


    private WallModel mWallItem;
    private NewUser mUser;
    private String mItemId;

    private FragmentOpportunitiesBinding b;

    public static OpportunitiesFragment getInstance(WallModel _wallItem) {
        OpportunitiesFragment fragment = new OpportunitiesFragment();
        fragment.setWallItem(_wallItem);
        return fragment;
    }

    public static OpportunitiesFragment getInstance(String _opportunityId) {
        OpportunitiesFragment fragment = new OpportunitiesFragment();
        fragment.setItemId(_opportunityId);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (b == null) {
            b = FragmentOpportunitiesBinding.inflate(inflater);
            b.setListener(this);
            if (mWallItem != null) setData();
            else getWallModelById();
        }
        return b.getRoot();
    }


    private void setUser() {

        mUser = new NewUser();
        WallUser wallUser = mWallItem.getUser();
        mUser.setJob(wallUser.getJob());
        mUser.setId(wallUser.getId());
        mUser.setUsername(wallUser.getName());
    }

    private void setData() {
        setUser();
        loadUserPicture(mWallItem.getUser().getAvatar());
        b.tvUserName.setText(mWallItem.getUser().getName());
        b.tvUserPosition.setText(mWallItem.getUser().getJob());
        b.tvUserCompany.setText(mWallItem.getUser().getCompany());
        b.tvUserCompanyWebsite.setText(mWallItem.getUser().getUrl());


        b.tvTitle.setText(mWallItem.getTitle());
        b.tvDescription.setText(mWallItem.getDescription());
        setFollowing(isFollowing());
        setIndustries();

    }

    public void setItemId(String _itemId) {
        this.mItemId = _itemId;
    }

    public void setWallItem(WallModel _wallItem) {
        this.mWallItem = _wallItem;
    }

    private boolean isFollowing() {
        if(mActivity.mUser.getFollow() == null || mActivity.mUser.getFollow().getUsers() == null)return false;
        List<String> list = mActivity.mUser.getFollow().getUsers();
        String userId = mWallItem.getUser().getId();
        return list.contains(userId);
    }

    private void setFollowing(boolean _isFollowing) {

        if (_isFollowing) {
            b.tvFollow.setText(getString(R.string.fragment_nearby_user_Following));
            b.tvFollow.setTextColor(getResources().getColor(R.color.follow_color));
        } else {
            b.tvFollow.setText(getString(R.string.fragment_nearby_user_Follow));
            b.tvFollow.setTextColor(getResources().getColor(R.color.blue));
        }
    }


    private void setIndustries() {
        if (mWallItem.getIndustries() != null && !mWallItem.getIndustries().isEmpty())
            createLabels(mWallItem.getIndustries(), b.llIndustriesLayout);
        else b.llIndustriesLayout.setVisibility(View.GONE);
    }


    private void createLabels(List<String> _list, LabelLayout _layout) {

        for (String str : _list) {

            TextView industry = (TextView) mActivity.getLayoutInflater().inflate(R.layout.item_label, null);
            industry.setText(str);
            _layout.addView(industry);
        }
    }


    private void onWallSuccess(WallModel wallModel) {

        mWallItem = wallModel;
        if (mWallItem != null && mWallItem.getId() != null)
            setData();
        else mActivity.replaceFragment(new WallFragment());
    }

    private void loadUserPicture(String _photoUrl) {
        if (_photoUrl != null)
            b.ivUserAvatar.setImageURI(Uri.parse(_photoUrl));

    }


    private void followUser(String _userId) {
        mUser.setIsFollowedByMe(true);
        mActivity.showProgress();


        Subscription subscription =
                RetrofitApi.getInstance()
                        .user()
                        .followUser(_userId)
                        .timeout(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onFollowSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }

    private void unFollowUser(String _userId) {
        mUser.setIsFollowedByMe(false);

        mActivity.showProgress();

        Subscription subscription = RetrofitApi.getInstance()
                .user()
                .unFollowUser(_userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFollowSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }


    public void onFollowSuccess(BaseResponse response) {
        mActivity.stopProgress();
        setFollowing(mUser.isFollowedByMe);

    }


    private void setUpInterested() {

        if (!mUser.isFollowedByMe) followUser(mWallItem.getUser().getId());
        else unFollowUser(mWallItem.getUser().getId());
    }

    private void sharePost() {
        mActivity.replaceFragmentWithBackStack(ShareFragment.getInstance(mWallItem));
    }

    private void setInterested(WallModel _model) {

        mActivity.showProgress();
        Subscription interestedSubscription = RetrofitApi.getInstance()
                .wall()
                .interestedWallPost(_model)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSetInterestedSuccess, this::handleError);


        mSubscriptions.add(interestedSubscription);
    }


    private void onSetInterestedSuccess(BaseResponse response) {
        Subscription subscription = RetrofitApi
                .getInstance()
                .chat()
                .getUserChats(mActivity.mUser.getId(), true)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onChatSuccess, this::handleError);
        mSubscriptions.add(subscription);
    }


    private void onChatSuccess(ChatResponseModel crm) {
        mActivity.stopProgress();
        String chatId = crm.findChatIdByUserId(mUser.getId());
        mActivity.replaceFragmentDown(ChatFragment.newInstance(chatId, mUser.getId(), getString(R.string.im_interested) + " \"" + mWallItem.getTitle() + "\""));
    }

    public void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }


    private void getWallModelById() {

        mActivity.showProgress();
        Subscription wallSubscription =
                RetrofitApi.getInstance()
                        .wall()
                        .getWallPost(mItemId)
                        .timeout(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(BaseResponse::getData)
                        .subscribe(this::onWallSuccess, this::handleError);

        mSubscriptions.add(wallSubscription);
    }

    // click listeners


    public void onBack(View view) {
        mActivity.onBackPressed();
    }

    public void onFollow(View view) {
        setUpInterested();
    }

    public void onInterested(View view) {
        setInterested(mWallItem);
    }

    public void onSharePost(View view) {
        sharePost();

    }

}
