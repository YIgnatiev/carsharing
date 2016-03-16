package com.vakoms.meshly.fragments.edit_user;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.hidden.Recolor;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;

import meshly.vakoms.com.meshly.BuildConfig;
import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentEditBinding;

/**
 * Created by Oleh Makhobey on 28.04.2015.
 * tajcig@ya.ru
 */
public class EditUserFragment extends BaseFragment<MainActivity> {



    private EditProfileFragment fragmentProfile;
    private EditPrivacyFragment fragmentPrivacy;
    private OnFragmentListener mOnFragmentListener;
    private boolean isProfile = true;
    private FragmentEditBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = FragmentEditBinding.inflate(inflater);
            b.setListener(this);
            findViews();
            replaceFragmentRight(fragmentProfile);

        }
        return b.getRoot();
    }


    private void findViews() {
        fragmentProfile = new EditProfileFragment();
        fragmentPrivacy = new EditPrivacyFragment();
        mOnFragmentListener = fragmentProfile;

    }



    public void replaceFragmentLeft(Fragment _fragment) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_right_in, R.animator.slide_right_out)
                .replace(R.id.flContainer_FragmentEdit, _fragment)

                .commitAllowingStateLoss();
    }

    public void replaceFragmentRight(Fragment _fragment){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_left_in, R.animator.slide_left_out)
                .replace(R.id.flContainer_FragmentEdit, _fragment)

                .commitAllowingStateLoss();
    }

    private void switchToPrivacy() {
        if (!isProfile) return;

        replaceFragmentLeft(fragmentPrivacy);
        isProfile = false;

        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds());

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }

        TransitionManager.beginDelayedTransition( b.flButtonContainer, set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);
        b.tvProfile.setTextColor(getResources().getColor(R.color.white_transparent_50));
        b.tvPrivacy.setTextColor(Color.WHITE);




    }

    private void switchToProfile() {
        if (isProfile) return;

        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds());

        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }

        TransitionManager.beginDelayedTransition(b.flButtonContainer, set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);


        b.tvProfile.setTextColor(Color.WHITE);
        b.tvPrivacy.setTextColor(getResources().getColor(R.color.white_transparent_50));


        replaceFragmentRight(fragmentProfile);
        isProfile = true;

    }


    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    //listener
    public void onPrivacy(View view){
        switchToPrivacy();

    }

    public void onProfile(View view){
        switchToProfile();

    }

    public void onSave(View view){
        if(mOnFragmentListener != null)mOnFragmentListener.onSaveClicked();

    }

    public void onMenu(View view){
        if(mOnFragmentListener != null) mOnFragmentListener.onMenuClicked();

    }

    public interface OnFragmentListener{

         void onMenuClicked();
         void onSaveClicked();


    }

}