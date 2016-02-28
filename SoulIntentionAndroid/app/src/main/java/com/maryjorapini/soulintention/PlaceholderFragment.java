package com.maryjorapini.soulintention;

/**
 * Created by Sasha on 18.11.2014.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maryjorapini.soulintention.fragments.DetailFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false);
        rootView.findViewById(R.id.section_label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentReplacer.replaceCurrentFragment(getActivity(), new DetailFragment());
                ((MainFragmentActivity)getActivity()).showDetailFragmentAB();
            }
        });
        return rootView;
    }
}