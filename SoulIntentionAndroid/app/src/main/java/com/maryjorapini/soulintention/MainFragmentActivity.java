package com.maryjorapini.soulintention;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.maryjorapini.soulintention.fragments.MainFragmentt;
import com.maryjorapini.soulintention.global.Constants;


public class MainFragmentActivity extends FragmentActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private RelativeLayout mUrl;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        findUI();
        setListeners();
        showTabFragment();

    }

    private void setListeners() {
        mUrl.setOnClickListener(this);
    }

    private void findUI() {
        mUrl = (RelativeLayout) findViewById(R.id.rlDump_FD);
    }

    private void showTabFragment() {
        FragmentReplacer.replaceFragmentWithoutBackStack(this, new MainFragmentt());
        showTabFragmentAB();
    }


    private void initActionBar() {
        View cView = getLayoutInflater().inflate(R.layout.action_bar, null);
        if (getActionBar() != null) {
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowCustomEnabled(true);
            getActionBar().setCustomView(cView);
            getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            sendActionBarListeners(cView);
        }
    }


    private void sendActionBarListeners(final View _view) {
        _view.findViewById(R.id.ivBack).setOnClickListener(this);
        ((SearchView) _view.findViewById(R.id.swSearch_AB)).setOnQueryTextFocusChangeListener(this);
        ((SearchView) _view.findViewById(R.id.swSearch_AB)).setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showTitle(true);
                return false;
            }
        });
//        setSearchViewOnClickListener(_view.findViewById(R.id.swSearch_AB), this);
        _view.findViewById(R.id.ivLike_AB).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                FragmentReplacer.popBackStack(this);
                showTabFragmentAB();
                break;
            case R.id.rlDump_FD:
                showWebAddress();
                break;
        }
    }

    private void showWebAddress() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.MAIN_URL));
        startActivity(intent);
    }

    public final void showBackButton(final boolean _doNeedToShow) {
        final View actionBarView = getActionBar().getCustomView();
        if (actionBarView == null) return;

        actionBarView.findViewById(R.id.ivBack).setVisibility(_doNeedToShow ? View.VISIBLE : View.GONE);
        actionBarView.findViewById(R.id.ivHome_AB).setVisibility(_doNeedToShow ? View.GONE : View.VISIBLE);
        actionBarView.findViewById(R.id.ivSort_AB).setVisibility(_doNeedToShow ? View.GONE : View.VISIBLE);

    }

    public final void showTitle(final boolean _doNeedToShow) {
        final View actionBarView = getActionBar().getCustomView();
        if (actionBarView == null) return;
        actionBarView.findViewById(R.id.tvTitle_AB).setVisibility(_doNeedToShow ? View.VISIBLE : View.GONE);

    }

    public final void showDetailFragmentAB() {
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        showLikeButton(true);
        showBackButton(true);
        showTitle(true);


    }

    public final void showTabFragmentAB() {
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        showLikeButton(false);
        showBackButton(false);
        showTitle(true);

    }


    public void showSearchButton(boolean _doNeedToShow){
        final View actionBarView = getActionBar().getCustomView();
        if (actionBarView == null) return;
        actionBarView.findViewById(R.id.swSearch_AB).setVisibility(_doNeedToShow ? View.VISIBLE : View.INVISIBLE);
        actionBarView.findViewById(R.id.ivSort_AB).setVisibility(_doNeedToShow ? View.VISIBLE : View.INVISIBLE);
        ((SearchView)getActionBar().getCustomView().findViewById(R.id.swSearch_AB)).setIconified(true);

    }

    public void showLikeButton(boolean _doNeedToShow) {
        final View actionBarView = getActionBar().getCustomView();
        if (actionBarView == null) return;

        actionBarView.findViewById(R.id.ivLike_AB).setVisibility(_doNeedToShow ? View.VISIBLE : View.GONE);
        actionBarView.findViewById(R.id.swSearch_AB).setVisibility(_doNeedToShow ? View.INVISIBLE : View.VISIBLE);
        ((SearchView)getActionBar().getCustomView().findViewById(R.id.swSearch_AB)).setIconified(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.SOCIAL_NETWORK_TAG);
        fragment.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b){
            showTitle(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (!FragmentReplacer.isEmpty(this)) {
            FragmentReplacer.popBackStack(this);
            showTabFragmentAB();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
