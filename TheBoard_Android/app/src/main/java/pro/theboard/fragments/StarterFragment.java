package pro.theboard.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.lapioworks.cards.R;

import pro.theboard.BaseActivity;
import pro.theboard.MainActivity;

/**
 * Created by Oleh Makhobey on 14.04.2015.
 * tajcig@ya.ru
 */
public class StarterFragment extends Fragment implements View.OnClickListener {

    private TextView tvTop;
    private TextView tvHeader;
    private boolean isSecondScreen;
    private boolean isTouched;
    private TextView tvSubmit;
    private BaseActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        mActivity = (MainActivity)activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_starter,container,false);
        findViews(rootView);
        setListeners();
        setData();
        mActivity.setToolbarTitle("");
        return rootView;
    }
    private void findViews(View _rootView){
        tvTop = (TextView)_rootView.findViewById(R.id.tv_top_message_StarterFragment);
        tvHeader = (TextView)_rootView.findViewById(R.id.tvHeader_StarterFragment);
        tvSubmit = (TextView)_rootView.findViewById(R.id.tv_ok_StarerFragment);
    }

    private void setListeners(){
        tvSubmit.setOnClickListener(this);
    }

    private void setData(){
        tvTop.setText(Html.fromHtml(getString(R.string.starter_message_bottom)));
        tvHeader.setText(Html.fromHtml(getString(R.string.starter_message_bottom_header)));
    }
    private void setSecondScreenData(){
        isTouched = true;
        Animation animation = new AlphaAnimation(1,0);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                setSecondScreenText();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvTop.startAnimation(animation);


    }
     private void setSecondScreenText(){

        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvTop.setText(Html.fromHtml(getString(R.string.starter_message_second)));
                tvHeader.setText(Html.fromHtml(getString(R.string.starter_message_second_header)));

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSecondScreen = true;
                isTouched = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvTop.startAnimation(animation);


    }



    private void proceedTouch(){
        if(isSecondScreen) {
            startCardsFragment();
        }else{
            setSecondScreenData();
        }
    }


    private void startCardsFragment(){
        mActivity.getQuestions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok_StarerFragment:
                if(!isTouched)proceedTouch();
                break;
        }
    }
}
