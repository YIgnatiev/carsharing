package com.vakoms.meshly.fragments.wellcome;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.WelcomeActivity;
import com.vakoms.meshly.adapters.IndustriesRecycleAdapter;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.interfaces.IndustriesItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Industry;
import com.vakoms.meshly.rest.RetrofitApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentChooseIndustriesBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/5/15.
 */
public class ChooseIndustriesFragment extends BaseFragment<WelcomeActivity> {
    private FragmentChooseIndustriesBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_industries, null, false);
            getIndustries();
        }
        return b.getRoot();
    }


    private void getIndustries(){

           Subscription subscription = RetrofitApi
                    .getInstance()
                    .meshly()
                    .getIndustries()
                    .retry(3)
                    .timeout(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(BaseResponse::getData)
                    .subscribe(this::onIndustriesSuccess, this::handleError);

            mSubscriptions.add(subscription);
        }



    private void onIndustriesSuccess(List<Industry> industries){
        b.rvIndustries.setLayoutManager(new LinearLayoutManager(mActivity));
        IndustriesRecycleAdapter adapter = new IndustriesRecycleAdapter(industries,mActivity.mUser.getIndustries());
        b.rvIndustries.setAdapter(adapter);
    }



    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }


}
