package com.vakoms.meshly.fragments.wellcome;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.WelcomeActivity;
import com.vakoms.meshly.adapters.FollowAdapter;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.interfaces.FollowItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentChoosePopularPeopleBinding;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/5/15.
 */
public class ChoosePopularPeopleFragment
        extends BaseFragment<WelcomeActivity>
        implements FollowItemListener {

    private boolean mIsRequestingFollowing;

    private FragmentChoosePopularPeopleBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_popular_people, null, false);
        getPopularPeople();
        return b.getRoot();
    }

    private void getPopularPeople(){
        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .getTopUsers(P.GPS.getLocation())
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::setList, this::handleError);
        mSubscriptions.add(subscription);
    }

    private void  setList(List<NewUser> users){
        FollowAdapter adapter = new FollowAdapter(users,this);
        b.rvPopular.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvPopular.setAdapter(adapter);
    }

    @Override
    public void onFollow(NewUser user) {
        user.setIsFollowedByMe(!user.getIsFollowedByMe());
        if (!user.getIsFollowedByMe()) {
            processRequest(RetrofitApi.getInstance().user().unFollowUser(user.getId()));
        } else {
            processRequest(RetrofitApi.getInstance().user().followUser(user.getId()));
        }
    }

    private void processRequest(Observable<BaseResponse> observable) {
        Subscription followSubscription = observable
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFollowSuccess, this::handleError);

        mSubscriptions.add(followSubscription);
    }


    private void onFollowSuccess(BaseResponse response){

    }

    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }
}
