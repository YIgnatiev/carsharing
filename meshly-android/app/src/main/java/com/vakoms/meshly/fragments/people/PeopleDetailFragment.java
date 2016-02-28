package com.vakoms.meshly.fragments.people;

import android.app.Fragment;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.ProfileEventsAdapter;
import com.vakoms.meshly.fragment_transition.fragment.FragmentTransitionLauncher;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.chat.ChatFragment;
import com.vakoms.meshly.fragments.events.EventsPageFragment;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.BlurController;
import com.vakoms.meshly.utils.NetworkUtil;
import com.vakoms.meshly.views.LabelLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentPeopleDetailBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/2/15.
 */
public class PeopleDetailFragment extends BaseFragment<MainActivity> implements View.OnClickListener, ObservableScrollViewCallbacks {


    private NewUser mUser;
    private String mUserId;
    private boolean mIsRequestingFollowing;
    public boolean isPastEventsActive;
    public final ObservableBoolean isPast = new ObservableBoolean(true);
    private int mUserContainerHeight;
    private List<Event> mUserEvents;
    private FragmentPeopleDetailBinding b;


    public static PeopleDetailFragment getInstance(NewUser _user) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
        fragment.setFragmentData(_user);

        return fragment;
    }

    public static PeopleDetailFragment getInstance(String userId) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
        fragment.setUserId(userId);
        return fragment;
    }


    public void setUserId(String userId) {
        mUserId = userId;
    }


    public void setFragmentData(NewUser user) {
        mUser = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = FragmentPeopleDetailBinding.inflate(inflater);

            if (mUserId != null) initFragment(mUserId);
            else if (mUser != null) initFragment(mUser);
        }
        return b.getRoot();
    }


    private void initFragment(String userId) {


        if(mActivity.mUser.getId().equals(userId)){
            b.ivMessageButton.setAlpha(.5f);
            b.ivFollowButton.setAlpha(.5f);
            b.ivFollowButton.setEnabled(false);
            b.ivMessageButton.setEnabled(false);
        }

        Subscription userSubscription = RetrofitApi
                .getInstance()
                .user()
                .getUserWithExtraData(userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(list -> list.get(0))
                .subscribe(this::initFragment, this::handleError);

        mSubscriptions.add(userSubscription);

    }



   float  mUserAvatarX;
    float mUserAvatarY;
    float mUserAvatarScaleFactor;
    float mTextHeight;
    private void initFragment(NewUser user) {

        mUser = user;
        b.setUser(mUser);
        b.setListener(this);
        b.svMain.setScrollViewCallbacks(this);

       b.svMain.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               return false;
           }
       });



        b.getRoot().post(() -> {

            mUserAvatarX = b.included.ivUserAvatar.getX();
            mUserAvatarY = b.included.ivUserAvatar.getY();
            mTextHeight = b.included.tvUserNameFragmentPeopleDetail.getY();
            mUserAvatarScaleFactor = ((float) b.included.rlActionBar.getHeight() / b.included.ivUserAvatar.getHeight()) * 0.8f;


            mUserContainerHeight = b.rlUserBackground.getMeasuredHeight();
            b.ivBackgroundFragmentPeopleDetail.getLayoutParams().height = mUserContainerHeight;
            b.overLay.getLayoutParams().height = mUserContainerHeight;

            b.emptyPlaceholder.getLayoutParams().height =(mUserContainerHeight - (int) getResources().getDimension(R.dimen.scroll_margin))/2;
            b.emptyPlaceholderBottom.getLayoutParams().height = (mUserContainerHeight - (int) getResources().getDimension(R.dimen.scroll_margin))/2;
        });


        setData(mUser);
        blurImage();
    }


    private void setData(NewUser _user) {
        createLabels(_user.getIndustries(), b.lIndustries);
        getUserEvents(_user.getId());
    }


    private void getUserEvents(String userId) {

        Subscription eventSubscription = RetrofitApi
                .getInstance()
                .events()
                .getAttedingEvents(userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::setUpEvents, this::handleError);

        mSubscriptions.add(eventSubscription);
    }

    public void setUpEvents(List<Event> events) {


        long currentTime = System.currentTimeMillis() / 1000;
        int counter = 0;
        ProfileEventsAdapter adapter = new ProfileEventsAdapter(mActivity, this);

        if (events != null) {
            b.llEvents.removeAllViews();
            mUserEvents = events;
            for (Event event : events) {
                if (counter == 3) break;

                if (isPast.get() && event.getEndTime() < currentTime) continue;
                if (isPast.get() && event.getEndTime() > currentTime) continue;


                View rootView = adapter.getInflatedView(event);
                b.llEvents.addView(rootView);
                counter++;
            }
            if (events.isEmpty()) b.tvEmptyView.setVisibility(View.VISIBLE);
        }

    }


    private void createLabels(List<String> _list, LabelLayout _layout) {
        if (_list != null)

            for (String label : _list) {
                TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.item_label, null);
                textView.setText(label);
                _layout.addView(textView);
            }
    }


    //listeners


    public void showAvatar(View view) {

        Fragment fragment = new PeopleImageFragment();
        FragmentTransitionLauncher
                .with(mActivity)
                .from(b.included.ivUserAvatar)
                .uri(mUser.getPicture())
                .prepare(fragment);


        mActivity.getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.flContainer, fragment)
                .commitAllowingStateLoss();


    }


    public void onBackClick(View view) {
        mActivity.onBackPressed();
    }


    public void onWriteClick(View view) {
        writeToUser(mUser.getId());
    }


    public void onShowMoreClicked(View view) {
        Toast.makeText(mActivity, "show more", Toast.LENGTH_SHORT).show();
    }


    public void onEventsUpcomig(View view) {
        isPast.set(true);
        setUpEvents(mUserEvents);

    }


    public void onPastEvents(View view) {
        isPast.set(false);
        setUpEvents(mUserEvents);
    }


    public void followUser(View view) {
        if (NetworkUtil.isNetworkAvailable(mActivity))
            if (!mIsRequestingFollowing) {
                if (mUser.getIsFollowedByMe()) {
                    mUser.setIsFollowedByMe(false);
                    processRequest(RetrofitApi.getInstance().user().unFollowUser(mUser.getId()));
                } else {
                    processRequest(RetrofitApi.getInstance().user().followUser(mUser.getId()));
                    mUser.setIsFollowedByMe(true);
                }

                mIsRequestingFollowing = true;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivArrow_ItemAttending:
                String id = (String) v.getTag();
                mActivity.replaceFragmentLeft(EventsPageFragment.instance(id));
                break;
        }
    }


    public void onDescription(View view){
        b.included.tvDescriptionFragmentPeopleDetail.setMaxLines(100);
    }

    private void processRequest(Observable<BaseResponse> observable) {
        Subscription followSubscription = observable
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::handleError)
                .subscribe(response -> mIsRequestingFollowing = false);

        mSubscriptions.add(followSubscription);
    }

    private void writeToUser(String userId) {

        Subscription writeToUserSubscription = RetrofitApi
                .getInstance()
                .chat()
                .getUserChats(userId, true)
                .map(BaseResponse::getData)
                .map(response -> response.findChatIdByUserId(userId))
                .subscribe(this::startChat, this::handleError);

        mSubscriptions.add(writeToUserSubscription);
    }

    private void startChat(String chatId) {
        mActivity.replaceFragmentDown(ChatFragment.newInstance(chatId, mUser.getId()));

    }


    private void blurImage() {
        new BlurController(mActivity, 20f)
                .blurImage(Uri.parse(mUser.getPicture()), b.ivBackgroundFragmentPeopleDetail);
    }


    public void handleError(Throwable e) {
        mActivity.handleError(e);
    }

    @Override
    public void onDestroyView() {

        super.onDestroy();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float flexibleRange = mUserContainerHeight- b.included.rlActionBar.getHeight();
        int minOverlayTransitionY = b.included.rlActionBar.getHeight() - mUserContainerHeight;
        float alpha = ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1);

        b.overLay.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        float translation = ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0);

        float fastAlpha = ScrollUtils.getFloat((flexibleRange )/(float) scrollY * 2  , 0, 1);

        b.included.tvHeader.setAlpha(fastAlpha);
        float relativeScaleFactorName = 1 - alpha*(1 - 0.7f);



        b.included.tvUserNameFragmentPeopleDetail.setScaleX(relativeScaleFactorName);
        b.included.tvUserNameFragmentPeopleDetail.setScaleY(relativeScaleFactorName);



        float yTitle = mTextHeight -(mTextHeight *alpha) + b.included.rlActionBar.getHeight()/4 *alpha;

        b.included.tvUserNameFragmentPeopleDetail.setY(yTitle);



        b.included.tvHeader.setAlpha(1 - alpha);

        b.included.ivUserAvatar.setTranslationY(translation);
        b.included.ivUserAvatar.setAlpha(1-alpha);

        b.ivBackgroundFragmentPeopleDetail.setTranslationY(translation);
        b.included.tvCreatedOpportunities.setTranslationY(translation);
        b.included.tvCreatedOpportunities.setAlpha(1-alpha);

        b.included.tvDeals.setTranslationY(translation);
        b.included.tvDeals.setAlpha(1-alpha);
        b.included.tvFollowers.setTranslationY(translation);
        b.included.tvFollowers.setAlpha(1 - alpha);
        b.included.tvFollowersLabel.setTranslationY(translation);
        b.included.tvFollowersLabel.setAlpha(1-alpha);
        b.included.tvUserJobFragmentPeopleDetail.setTranslationY(translation);
        b.included.tvUserJobFragmentPeopleDetail.setAlpha(1-alpha);
        b.included.vwDivider.setTranslationY(translation);
        b.included.vwDivider.setAlpha(1- alpha);
        b.included.tvDescriptionFragmentPeopleDetail.setTranslationY(translation);
        b.included.tvDescriptionFragmentPeopleDetail.setAlpha(1- alpha);

        b.overLay.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, getResources().getColor(R.color.map_color)));
    }




    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}


