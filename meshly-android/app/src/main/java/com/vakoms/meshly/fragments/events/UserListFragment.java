package com.vakoms.meshly.fragments.events;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.BaseActivity;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.UserListAdapter;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.UserItemListListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Following;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.databinding.FragmentUserListBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserListFragment extends BaseFragment<MainActivity> implements UserItemListListener {

    private String mEventId;
    private FragmentUserListBinding b;
    private String title;

    private BaseActivity mActivity;


    public static UserListFragment instance(String eventId) {
        UserListFragment frag = new UserListFragment();
        frag.setTitle("Attendees");
        frag.setEventId(eventId);
        return frag;
    }

    public static UserListFragment getUserFollowersInstance() {
        UserListFragment frag = new UserListFragment();
        frag.setTitle("Following");
        return frag;
    }

    public void setEventId(String _eventId) {
        this.mEventId = _eventId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = FragmentUserListBinding.inflate(inflater, container, false);
            mActivity = (BaseActivity) getActivity();
            b.setData(this);
            if (mEventId != null) getSubscribers();
            else getFollowers();
        }
        return b.getRoot();

    }

    private void getFollowers() {
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .getUserFollowing()
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(Following::getFollowing)
                .subscribe(this::onUsersSuccess, this::handleError);
        mSubscriptions.add(subscription);
    }


    private void getSubscribers() {
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .getEventSubscribers(mEventId)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onUsersSuccess, this::handleError);
        mSubscriptions.add(subscription);

    }


    public void onBackClicked(View view) {
        mActivity.onBackPressed();
    }


    private void onUsersSuccess(List<NewUser> users) {


        Collections.sort(users,(left, right)-> left.getUsername().compareTo(right.getUsername()));


        mActivity.stopProgress();
        UserListAdapter adapter = new UserListAdapter(users);
        adapter.setListener(this);
        b.rvUsers.setItemAnimator(new DefaultItemAnimator());
        b.rvUsers.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvUsers.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    @Override
    public void onAvatarClicked(String userId) {

        mActivity.replaceFragmentLeft(PeopleDetailFragment.getInstance(userId));

    }

    @Override
    public void onItemClicked(String userId) {
        mActivity.replaceFragmentLeft(PeopleDetailFragment.getInstance(userId));

    }


}