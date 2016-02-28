package com.vakoms.meshly.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.hidden.Recolor;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.events.AddEventFragment;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentAboutBinding;

/**
 * Created by taras.melko on 15.09.2014.
 */
public class AboutFragment extends BaseFragment<MainActivity> {

    private FragmentAboutBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater,R.layout.fragment_about,null,false);
        b.setListener(this);
        b.tvEmail.setText(Html.fromHtml(getString(R.string.fragment_about_MeshlyEmail)));

        return b.getRoot();
    }


    private void switchEvents(){

        TransitionSet set = new TransitionSet();
        set.addTransition(new Recolor());
        set.addTransition(new ChangeBounds());
        TransitionManager.beginDelayedTransition(b.llContainer,set);

        b.tvEvents.setBackgroundResource(R.drawable.blue_rounded_rectangle);
        b.tvEvents.setTextColor(Color.parseColor("#142B33"));
        b.tvPartners.setBackgroundResource(R.drawable.blue_rounded_stroke);
        b.tvPartners.setTextColor(Color.parseColor("#66D6FF"));
        b.llParteners.setVisibility(View.GONE);
        b.llEvents.setVisibility(View.VISIBLE);

    }

    private void switchAbout(){

        TransitionSet set = new TransitionSet();
        set.addTransition(new Recolor());
        set.addTransition(new ChangeBounds());

        TransitionManager.beginDelayedTransition(b.llContainer,set);

        b.tvEvents.setBackgroundResource(R.drawable.blue_rounded_stroke);
        b.tvEvents.setTextColor(Color.parseColor("#66D6FF"));
        b.tvPartners.setBackgroundResource(R.drawable.blue_rounded_rectangle);
        b.tvPartners.setTextColor(Color.parseColor("#142B33"));
        b.llParteners.setVisibility(View.VISIBLE);
        b.llEvents.setVisibility(View.GONE);
    }

    private void startChooser(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.meshly_email)});
        //intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        //intent.putExtra(Intent.EXTRA_TEXT, "mail body");
        startActivity(Intent.createChooser(intent, ""));
    }

    private void openMenu(){
        mActivity.openMainMenu();
    }

    private void submitEvent(){
        mActivity.replaceFragmentLeft(new AddEventFragment());
    }

    public void onEvents(View view){
        switchEvents();
    }

    public void onPartners(View view){
        switchAbout();
    }

    public void onEmail(View view){
        startChooser();
    }

    public void onMenu(View view){
        openMenu();
    }

    public void onSubmitEvent(View view){
        submitEvent();
    }

    @Override
    protected void handleError(Throwable throwable) {

    }
}