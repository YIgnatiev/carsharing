package com.vakoms.meshly.fragments.events;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.EventsPageAdapter;
import com.vakoms.meshly.adapters.PeopleAdapter;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.databinding.EventsFragmentB;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by taras.melko on 05.09.2014.
 */

public class EventsFragment extends BaseFragment<MainActivity> implements LoaderManager.LoaderCallbacks<Cursor> {

    private EventsPageAdapter adapter;
    private List<Event> mEventsList = new ArrayList<>();
    private Location mLocation;
    private int eventPosition = -1;
    private String scrollToId ;
    private int position = 0;

    private EventsFragmentB b;

    /**
     * Fragment will scroll list to specified event id (used from Push Notification)
     *
     * @param id event id
     */
    public void setScrollToId(String id) {
        this.scrollToId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = EventsFragmentB.inflate(inflater);
            b.setListener(this);
            mLocation = P.GPS.getLocation();
            getLoaderManager().restartLoader(1, null, this);
            addMenuListener(b.getRoot(),mActivity);
        }
        return b.getRoot();
    }


    private void updateCounter() {
        b.tvCounter.setText((position + 1) + "/" + mEventsList.size());
    }

    private void setupView() {
        if (mEventsList == null)
            mEventsList = new ArrayList<>();
        adapter = new EventsPageAdapter(getFragmentManager(), mEventsList, mLocation);

        b.vvPager.setAdapter(adapter);
        if (mEventsList.size() > 0) {
            updateCounter();
//            setupImage(position);
        }

        b.vvPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int touchedId = 0;
            boolean isTouched = false;
            boolean isFling = false;
            boolean hasPagerBackground = false;

            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (isTouched) {
                    touchedId = i;
                }


            }

            @Override
            public void onPageSelected(int i) {
                if (mEventsList.size() > 0) {

                    position = i;
                    updateCounter();
                }
            }


            @Override
            public void onPageScrollStateChanged(int i) {
                isTouched = (i == 1);
                isFling = (i == 2);
            }
        });


        //find event position to scroll to (from push notification)
        if (scrollToId != null) {
            for (int index = 0; index < mEventsList.size(); index++) {
                if (mEventsList.get(index).getId().equals(scrollToId)) {
                    eventPosition = index;
                    b.vvPager.setCurrentItem(eventPosition, false);
                    break;
                }
            }
        }

    }

    String lastImageUrl = "";



    private void getEventsTask() {
        mActivity.showProgress();

        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .getEvents(P.GPS.getLatitude(), P.GPS.getLongitude())
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onEventsReceived, this::handleException);
        mSubscriptions.add(subscription);
    }


    public void onEventsReceived(List<Event> eventList) {

        mEventsList.clear();
        mEventsList.addAll(eventList);

        Collections.sort(mEventsList,(left, right)->  right.getDistance() > left.getDistance() ? -1 : 1 );
        mActivity.stopProgress();
        if (mEventsList.size() > 0) {
            setupView();
        }
    }

    public void handleException(Throwable e) {
        mActivity.stopProgress();
    }


    private void moveUp() {
        if (mEventsList.size() > 0)
            if (position > 0) {
                position--;
                b.vvPager.setCurrentItem(position);
                updateCounter();
            }
    }

    private void moveDown() {
        if (mEventsList.size() > 0)
            if (position < mEventsList.size() - 1) {
                position++;
                b.vvPager.setCurrentItem(position);
                updateCounter();
            }
    }

    @Override
    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }


    //listeners

    public void onMenuClick(View view) {
        mActivity.openMainMenu();

    }

    public void onUpArrow(View view) {
        moveUp();
    }

    public void onDownArrow(View view) {
        moveDown();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] columns = MeshlyColumns.getColumns(Event.class);
        return new CursorLoader(mActivity, UserProvider.EVENTS_URI, columns, null, null, null);
    }


    private boolean isAllowed = true;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (isAllowed) {
            UserDAO dao = UserDAO.getInstance();
            List<Event> events = new ArrayList<>();

            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                events.add(dao.getEvent(data));
            }

            onEventsReceived(events);
            isAllowed = false;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    //    @Override
//    public void onUpdate(Event item) {
//
//
//        int position = mEventsList.indexOf(item);
//        mEventsList.set(position, item);
//        adapter.notifyDataSetChanged();
//    }


}