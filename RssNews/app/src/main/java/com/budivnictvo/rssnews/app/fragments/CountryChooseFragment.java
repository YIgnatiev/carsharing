package com.budivnictvo.rssnews.app.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.budivnictvo.rssnews.app.MainActivity;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.core.AppConstants;

/**
 * Created by Администратор on 18.12.2014.
 */
public class CountryChooseFragment extends Fragment implements View.OnClickListener{
    ImageView ivCountryUA;
    ImageView ivCountryRU;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_country_choose,container,false);
        findUI(rootView);
        setListeners();
        setActionBar();
        return rootView;
    }

    private void findUI(View _rootView){
        ivCountryRU = (ImageView)_rootView.findViewById(R.id.ivCountryRussia);
        ivCountryUA = (ImageView)_rootView.findViewById(R.id.ivCountryUkraine);
    }

    private void setListeners(){
        ivCountryRU.setOnClickListener(this);
        ivCountryUA.setOnClickListener(this);
    }


    private void setActionBar(){
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.setBackButtonVisible(false);
    }
    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()){

            case R.id.ivCountryRussia:
                bundle.putInt(AppConstants.COUNTRY_CHOSEN, AppConstants.RUSSIAN);
                break;
            case R.id.ivCountryUkraine:
                bundle.putInt(AppConstants.COUNTRY_CHOSEN, AppConstants.UKRAINEAN);
                break;
        }
        Fragment fragment = new ChannelChooseFragment();
        fragment.setArguments(bundle);
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.replaceFragmentWithBackstack(fragment);
    }
}
