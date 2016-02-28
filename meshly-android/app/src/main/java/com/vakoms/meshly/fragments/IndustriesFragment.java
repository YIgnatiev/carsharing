package com.vakoms.meshly.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.vakoms.meshly.adapters.IndustriesAdapter;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Industry;
import com.vakoms.meshly.rest.RetrofitApi;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentIndustriesBinding;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Oleh Makhobey on 10.05.2015.
 * tajcig@ya.ru
 */
public class IndustriesFragment extends Fragment  {
    private List<String> mIndustries;
    private IndustriesAdapter mAdapter;
    private IndustriesListener listener;
    private Subscription subscription;
    private FragmentIndustriesBinding b;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_industries, container, false);

        b = DataBindingUtil.bind(rootView);
        b.setListener(this);

        getIndustries();
        return rootView;
    }

    public void getIndustries() {
        b.progressBar.setVisibility(VISIBLE);
        subscription = RetrofitApi
                .getInstance()
                .meshly()
                .getIndustries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getIndustriesObserver());
    }


    @Override
    public void onStop() {
        if(subscription != null)subscription.unsubscribe();
        super.onStop();
    }

    private void setUpList(List<Industry> _list) {
        List<String> stringList = new ArrayList<>();
        for (Industry item : _list) {
            stringList.add(item.getName());
        }

        mAdapter = new IndustriesAdapter(getActivity(), mIndustries, stringList);
        b.lvIndustries.setAdapter(mAdapter);

    }

    public void setIndustriesListener(IndustriesListener _listener) {
        this.listener = _listener;
    }

    public void setIndustries(List<String> _industries) {
        if (_industries == null) mIndustries = new ArrayList<>();
        else this.mIndustries = _industries;
    }

    public void onListItemClicked(AdapterView<?> parent, View view, int position, long id) {
        String industry = (String) parent.getItemAtPosition(position);
        ImageView checkBox = (ImageView) view.findViewById(R.id.ivCheckbox_ItemIndustries);
        if (mIndustries.contains(industry)) {
            mIndustries.remove(industry);
            mAdapter.switchIcons(checkBox, false);
        } else {
            mIndustries.add(industry);
            mAdapter.switchIcons(checkBox, true);
        }
    }

    public void onBackClicked(View view) {
        getActivity().onBackPressed();
    }

    public void onSaveClicked(View view) {
        listener.onIndustriesChanged(mIndustries);
        getFragmentManager().popBackStack();
    }



    public Observer<? super BaseResponse<List<Industry>>> getIndustriesObserver() {
        return new Observer<BaseResponse<List<Industry>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                b.progressBar.setVisibility(GONE);
                Toast.makeText(getActivity(), "Network error. ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(BaseResponse<List<Industry>> listBaseResponse) {
                setUpList(listBaseResponse.getData());
                b.progressBar.setVisibility(GONE);
            }
        };
    }

    public interface IndustriesListener {
        void onIndustriesChanged(List<String> _industries);
    }
}
