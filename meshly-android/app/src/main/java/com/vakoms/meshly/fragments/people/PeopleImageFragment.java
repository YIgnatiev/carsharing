package com.vakoms.meshly.fragments.people;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragment_transition.fragment.FragmentTransition;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentPeopleImageBinding;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/8/15.
 */
public class PeopleImageFragment extends Fragment {

    MainActivity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentPeopleImageBinding b = DataBindingUtil.inflate(inflater, R.layout.fragment_people_image, container, false);
        mActivity = (MainActivity)getActivity();
        b.setListener(this);
      FragmentTransition
                .with(this)
                .to(b.ivUserAvatar)
                .duration(300)
                .interpolator(new FastOutSlowInInterpolator())
                .start(savedInstanceState)
                .startExitListening();

        return b.getRoot();
    }


    public void onBack(View view){
        mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));

    }
}
