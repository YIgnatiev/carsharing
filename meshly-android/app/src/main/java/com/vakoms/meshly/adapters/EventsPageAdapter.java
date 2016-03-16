package com.vakoms.meshly.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.vakoms.meshly.fragments.events.EventsPageFragment;
import com.vakoms.meshly.models.Event;

import java.util.List;

/**
 * Created by taras.melko on 9/12/14.
 */
public class EventsPageAdapter extends FragmentStatePagerAdapter {

    private int pageCount = 0;
    private List<Event> mList;
    private Location location;

    public EventsPageAdapter(FragmentManager fm, List<Event> list, Location location) {
        super(fm);
        this.pageCount = list.size();
        this.mList = list;
        this.location = location;
    }

    @Override
    public Fragment getItem(int position) {
        Event event = mList.get(position);
        return EventsPageFragment.instance(event);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}