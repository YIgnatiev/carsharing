package com.maryjorapini.soulintention.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maryjorapini.soulintention.R;

import java.util.ArrayList;

/**
 * Created by Sasha on 21.08.2014.
 */
public class CompanyDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentActivity mActivity;
    private ArrayList<Fragment> mFragments;

    public CompanyDetailsFragmentPagerAdapter(final FragmentActivity _activity,
                                              final ArrayList<Fragment> _fragments,
                                              final FragmentManager _fragmentManager) {
        super(_fragmentManager);
        mActivity = _activity;
        mFragments = _fragments;
    }

    @Override
    public final Fragment getItem(final int _position) {
        return mFragments.get(_position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final String[] titles = mActivity.getResources().
                getStringArray(R.array.connections_details_tabs);
        return titles[position];
    }
}

