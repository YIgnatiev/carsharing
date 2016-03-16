package com.vakoms.meshly.fragments.events;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.ShareWithLinkedinFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.rest.RetrofitApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.EventsPage;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by taras.melko on 05.09.2014.
 */
public class EventsPageFragment extends BaseFragment<MainActivity> {


    private EventsPage b;


    private Event mEvent;

    private String mEventId;
    private boolean isGoing;

    public static EventsPageFragment instance(Event _event) {
        EventsPageFragment fragment = new EventsPageFragment();

        fragment.setEvent(_event);
        return fragment;
    }


    public static EventsPageFragment instance(String _eventId){
        EventsPageFragment fragment = new EventsPageFragment();
        fragment.setEventId(_eventId);
        return fragment;
    }



    public void setEventId(String _eventId){
        this.mEventId= _eventId;
    }

    public void setEvent(Event _event) {
        this.mEvent = _event;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = EventsPage.inflate(inflater);
            b.setListener(this);

            if (mEvent != null) {
                setupData();
            } else {
                b.rlActionBar.setVisibility(View.VISIBLE);
                getEventById();
            }
        }
        return b.getRoot();
    }


    private void setupData() {

        b.setEvent(mEvent);
        updateList();
        isGoing = eventChecker(mEvent.getId());
        setupGoing(isGoing);

    }

    private void updateList() {
        if (mEvent.getSubscribers().size() > 0) {

            int equal = mEvent.getSubscribersNumber() - mEvent.getSubscribers().size();
            if (equal > 0) {
                b.tvAllreadyGoingCount.setText("+" + equal);
            } else {
                b.tvAllreadyGoingCount.setVisibility(View.GONE);
            }


            switch (mEvent.getSubscribers().size()) {
                case 1:
                    b.ivFirstSubscriber.setVisibility(View.VISIBLE);
                    b.ivFirstSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(0)));
                    break;
                case 2:
                    b.ivFirstSubscriber.setVisibility(View.VISIBLE);
                    b.ivFirstSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(0)));
                    b.ivSecondSubscriber.setVisibility(View.VISIBLE);
                    b.ivSecondSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(1)));
                    break;
                case 3:
                    b.ivFirstSubscriber.setVisibility(View.VISIBLE);
                    b.ivFirstSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(0)));
                    b.ivSecondSubscriber.setVisibility(View.VISIBLE);
                    b.ivSecondSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(1)));
                    b.ivThirdSubscriber.setVisibility(View.VISIBLE);
                    b.ivThirdSubscriber.setImageURI(Uri.parse(mEvent.getSubscribers().get(2)));
                    break;
            }

        } else {
            b.tvAllreadyGoingCount.setVisibility(View.GONE);
        }
    }

    private boolean eventChecker(String eventId) {
        if (mActivity.mUser.getFollow() == null || mActivity.mUser.getFollow().getEvents() == null)
            return false;
        else return mActivity.mUser.getFollow().getEvents().contains(eventId);

    }


    public void showDialogGoing() {
        new AlertDialog.Builder(getActivity()).setCancelable(true)
                .setTitle(getActivity().getString(R.string.fragment_events_confirmText))
                .setPositiveButton(getActivity().getString(R.string.fragment_events_Cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(R.drawable.ic_launcher).setNegativeButton(getActivity().getString(R.string.fragment_events_Yes), (dialog, which) -> {
                    followEvent();
                })
                .show();
    }

    private void showDialogGoingWithLinkedInSharing() {
        new AlertDialog.Builder(getActivity()).setCancelable(true)
                .setTitle(getActivity().getString(R.string.fragment_events_confirmText))
                .setPositiveButton(getActivity().getString(R.string.fragment_events_Cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(R.drawable.ic_launcher).setNegativeButton(getActivity().getString(R.string.fragment_events_Yes), (dialog, which) -> {
                    followEvent();
                })
                .setNeutralButton(getActivity().getString(R.string.fragment_events_YesShareViaLinkedIn), (dialog, which) -> {
                    followEventWithLinkedin();
                })
                .show();
    }

    private void showDialogCancel() {
        new AlertDialog.Builder(getActivity()).setCancelable(true)
                .setTitle(String.format(getActivity().getString(R.string.fragment_events_CancelText), mEvent.getName()))
                .setPositiveButton(getString(R.string.fragment_events_Cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.fragment_events_Yes), (dialog, which) -> {
                    unfollowEvent();
                }).setIcon(R.drawable.ic_launcher).show();
    }

    private void showDialog(String _message, String _tag) {


        View rootView = mActivity.getLayoutInflater().inflate(R.layout.dialog_confirm,null);

        new AlertDialog.Builder(getActivity()).setCancelable(true)

                .setView(rootView)
                .setTitle(_message)
                .setPositiveButton(getString(R.string.fragment_events_Cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.fragment_events_Yes), (dialog, which) -> {
                    unfollowEvent();
                }).setIcon(R.drawable.ic_launcher).show();
    }


    private void setupGoing(boolean flag) {
        if (flag) {
            b.ivGoing.setBackgroundResource(R.drawable.ic_event_check);
            b.tvGoing.setText(getActivity().getString(R.string.fragment_events_GoingText));
            b.tvGoing.setTextColor(getActivity().getResources().getColor(android.R.color.white));
        } else {
            b.ivGoing.setBackgroundResource(R.drawable.ic_event_uncheck);
            b.tvGoing.setText(getActivity().getString(R.string.fragment_events_GoingTextAsk));
            b.tvGoing.setTextColor(Color.parseColor("#7B7B79"));
        }
    }

    private void followEventWithLinkedin() {
        Subscription subscription = RetrofitApi.getInstance()
                .events()
                .followEvent(mEvent.getId(), mActivity.mUser.getId())

                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::replaceLinkedInFragment, this::handleError);
        mSubscriptions.add(subscription);

    }





    private void replaceLinkedInFragment (BaseResponse response) {

        setupGoing(true);
        isGoing = true;

        onFollowSuccess(null);

        Fragment fragment = ShareWithLinkedinFragment.instance(mActivity.mUser.getUsername(),
                String.format(getActivity().getString(R.string.fragment_events_ImGoingTo),
                        mEvent.getName(),
                        mEvent.getCity()),
                false);
        mActivity.replaceFragment(fragment);

    }


    public void getEventById(){
        mActivity.showProgress();


        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .getEventById(mEventId)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::startEventFragment, this::handleError);

        mSubscriptions.add(subscription);

    }

    private void startEventFragment(Event _event){
        mActivity.stopProgress();
        mEvent = _event;
        if(mEvent != null && mEvent.getSubscribers() != null)setupData();
        else mActivity.replaceFragment(new EventsFragment());
    }




    private void followEvent() {

        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .followEvent(mEvent.getId(), mActivity.mUser.getId())
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFollowSuccess,this::handleError);

        mSubscriptions.add(subscription);

    }


    private void onFollowSuccess(BaseResponse response){
        setupGoing(true);
        isGoing = true;
        if(mActivity.mUser.getFollow() != null && mActivity.mUser.getFollow().getEvents() != null){
            List<String> events = mActivity.mUser.getFollow().getEvents();
            events.add(mEvent.getId());
        }
    }




    private void unfollowEvent() {
        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .unfollowEvent(mEvent.getId(), mActivity.mUser.getId())
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUnfollowSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }

    private void onUnfollowSuccess(BaseResponse response){
        setupGoing(false);
        isGoing = false;

       if(mActivity.mUser.getFollow() != null && mActivity.mUser.getFollow().getEvents() != null){
           List<String> events = mActivity.mUser.getFollow().getEvents();
           events.remove(mEvent.getId());
       }
    }

    public void handleError(Throwable throwable){

        mActivity.handleError(throwable);
    }








    private boolean existsLinkedInToken(){
        return !(mActivity.mUser.getLinkedin() == null || mActivity.mUser.getLinkedin().getToken() == null || mActivity.mUser.getLinkedin().getToken().isEmpty());
    }

    private void goingToEvent() {
        if (isGoing) {
            showDialogCancel();
        } else {
            if (existsLinkedInToken()){
                showDialogGoingWithLinkedInSharing();
            } else {
                showDialogGoing();
            }
        }
    }


    private void openLink() {
        if (mEvent.getUrl() != null) {
            Intent loadUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(mEvent.getUrl()));
            startActivity(loadUrl);
        }
    }


    //listener

    public void onGoingClicked(View view){
        goingToEvent();

    }

    public void onLinkClicked(View view){
        openLink();

    }

    public void onAttendiesClicked(View view){
        mActivity.replaceFragmentWithBackStack(UserListFragment.instance(mEvent.getId()));

    }
    public void onBackClicked(View view){
        mActivity.getFragmentManager().popBackStack();

    }




}