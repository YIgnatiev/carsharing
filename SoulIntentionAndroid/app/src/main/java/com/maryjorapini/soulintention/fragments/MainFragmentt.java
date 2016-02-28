package com.maryjorapini.soulintention.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maryjorapini.soulintention.MainFragmentActivity;
import com.maryjorapini.soulintention.R;
import com.maryjorapini.soulintention.adapters.CompanyDetailsFragmentPagerAdapter;
import com.maryjorapini.soulintention.custom_views.custom_view_pager.TabPageIndicator;
import com.maryjorapini.soulintention.global.Constants;

import java.util.ArrayList;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link MainFragmentt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragmentt extends Fragment implements ViewPager.OnPageChangeListener {

    private FragmentActivity mActivity;
    private ViewPager vpTabs_FCD;
    private TabPageIndicator indicator;
    private final int SOULS_TAB = 0;
    private final int AUTHOR_TAB = 1;
    private final int FAVORITES_TAB = 2;
    private CompanyDetailsFragmentPagerAdapter mFragmentAdapter;

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment mainFragment.
     */

    public static MainFragmentt newInstance(String param1) {
        MainFragmentt fragment = new MainFragmentt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragmentt() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            ((MainFragmentActivity) mActivity).showTabFragmentAB();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tabbed_main, container, false);
        findUi(view);
        initAdapter();
        return view;
    }

    private final void initializeViewPager() {
        final ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        fragments.add(SOULS_TAB, SoulFragment.newInstance(Constants.POST_TYPE_SOULS));
        fragments.add(AUTHOR_TAB, new AuthorFragment());
        fragments.add(FAVORITES_TAB, SoulFragment.newInstance(Constants.POST_TYPE_FAVOURITE));

        mFragmentAdapter = new CompanyDetailsFragmentPagerAdapter(mActivity, fragments, getChildFragmentManager());

        vpTabs_FCD.setAdapter(mFragmentAdapter);

        indicator.setViewPager(vpTabs_FCD);
        mFragmentAdapter.notifyDataSetChanged();
    }

    /**
     * Initialization of fragment adapter
     */
    private void initAdapter() {
        if (mFragmentAdapter == null) {
            initializeViewPager();
        } else {
            vpTabs_FCD.setAdapter(mFragmentAdapter);
            indicator.setViewPager(vpTabs_FCD);
        }
        indicator.setOnPageChangeListener(this);
    }

    /**
     * Initializes all UI objects from current fragment view.
     *
     * @param _view current fragment view
     */
    private void findUi(final View _view) {
        vpTabs_FCD = (ViewPager) _view.findViewById(R.id.vpTabs_FCND);
        indicator = (TabPageIndicator) _view.findViewById(R.id.connectionTabIndicator);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i != AUTHOR_TAB){
            ((MainFragmentActivity)mActivity).showSearchButton(true);
        } else {
            ((MainFragmentActivity)mActivity).showSearchButton(false);
            ((MainFragmentActivity)mActivity).showTitle(true);

        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        ((MainFragmentActivity)mActivity).hideSoftKeyboard();
    }

}
