package com.vakoms.meshly.fragments.people;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.hidden.Recolor;
import com.vakoms.meshly.BaseActivity;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.PeopleAdapter;
import com.vakoms.meshly.databases.MeshlyColumns;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.databases.UserProvider;
import com.vakoms.meshly.fragment_transition.FragmentTransactionHelper;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.chat.ChatFragment;
import com.vakoms.meshly.interfaces.PeopleItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.ReactiveScroll;
import com.vakoms.meshly.utils.SearchAnimator;
import com.vakoms.meshly.views.ResideMenu;

import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentPeopleBinding;
import meshly.vakoms.com.meshly.databinding.PeopleItem;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;


/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/30/15.
 */
public class PeopleFragment extends BaseFragment<MainActivity> implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor>, PeopleItemListener {
    private boolean isLoading;
    private SearchAnimator mSearchAnimator;

    private String mSearchWord = "";
    private FragmentPeopleBinding b;

    public ObservableBoolean isDiscoverMode = new ObservableBoolean(false);


    private int GET_PEOPLE = 777;


    private UserDAO mDao;
    Subscription mUserSubscription = Subscriptions.unsubscribed();
    private boolean isLoadAllowed;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = FragmentPeopleBinding.inflate(inflater, null, false);
            mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
            b.setListener(this);
            initViews();
            initListView();
            subscribeToScroll();
            subscribeToDiscoverScroll();
            mDao = UserDAO.getInstance();
            subScribeEditText();
            isLoadAllowed = true;
            getLoaderManager().restartLoader(GET_PEOPLE, null, this);
            getDiscoverUsers();
            addMenuListener(b.getRoot(),mActivity);

        }
        return b.getRoot();
    }


    public void initViews() {
        b.swipeToRefresh.setOnRefreshListener(this);
        setSearchView();

    }


    private void initListView() {


        b.rvDiscoverPeople.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvDiscoverPeople.setAdapter(new PeopleAdapter(this));


        b.rvPeople.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvPeople.setAdapter(new PeopleAdapter(this));


    }


    private void subscribeToScroll() {
        new ReactiveScroll().observeScroll(b.rvPeople)
                .distinctUntilChanged()
                .buffer(2, 1)
                .map(list -> {
                    paginate(list.get(0), b.rvPeople);
                    return list.get(0) < list.get(1);
                })
                .distinctUntilChanged()
                .subscribe(this::handleTopBackground);

    }


    private void subscribeToDiscoverScroll() {
        new ReactiveScroll().observeScroll(b.rvDiscoverPeople)
                .distinctUntilChanged()
                .buffer(2, 1)
                .map(list -> list.get(0) < list.get(1))
                .distinctUntilChanged()
                .subscribe(this::handleTopBackground);

    }


    private boolean isLast;

    private void paginate(int lastItem, RecyclerView recyclerView) {


        int listSize = recyclerView.getAdapter().getItemCount();
        if (lastItem > 0 && lastItem > listSize - 5 && mUserSubscription.isUnsubscribed() && !isLast) {

            addMore(Command.PAGINATE, (listSize / 10 + 1));
        }

    }


    private void getDiscoverUsers() {

        mActivity.showProgress();

        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .getDiscoverUsers()
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::onDiscoverSuccess, this::handleError);

        mSubscriptions.add(subscription);
    }


    private void onDiscoverSuccess(List<NewUser> users) {
        ((PeopleAdapter) b.rvDiscoverPeople.getAdapter()).getList().addAll(users);
        b.rvDiscoverPeople.getAdapter().notifyDataSetChanged();

        mActivity.stopProgress();
    }

    private void handleTopBackground(boolean isSwipingTop) {


        TransitionSet set = new TransitionSet();
        set.addTransition(new Slide(Gravity.TOP));
        set.addTransition(new ChangeBounds());
        set.addTransition(new Fade());
        TransitionManager.beginDelayedTransition(b.rlActionBar, set);

        if (isSwipingTop) b.rlTop.setVisibility(View.GONE);
        else b.rlTop.setVisibility(View.VISIBLE);

    }


    private void observeEmptyView(List<NewUser> list) {
        Observable.from(list)
                .isEmpty()
                .map(isEmpty -> isEmpty ? View.GONE : View.VISIBLE)
                .subscribe();
    }

    private void setSearchView() {
        mSearchAnimator = new SearchAnimator(b.etSearch);
        mSearchAnimator.setViewToHide(b.ivMenu, b.tvTitle);
        mSearchAnimator.setViewToShow(b.tvCancel);

    }

    private void startFragment(View view, NewUser _user) {


        mActivity.replaceFragmentLeft(PeopleDetailFragment.getInstance(_user));




}


    public void subScribeEditText() {
        WidgetObservable.text(b.etSearch).debounce(500, TimeUnit.MILLISECONDS)
                .map(OnTextChangeEvent::text)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::search);


    }

    private void search(String searchWord) {
        mSearchWord = searchWord;
        addMore(Command.SEARCH, 1);
    }

    @Override
    public void onRefresh() {
        BaseActivity.TriggerRefresh(new Bundle());
        mSearchWord = "";
        addMore(Command.REFRESH, 1);


    }

    private void addMore(Command command, int page) {

        isLoading = true;
        mUserSubscription = RetrofitApi
                .getInstance()
                .user()
                .getUsers(mSearchWord, P.GPS.getLatitude(), P.GPS.getLongitude(), page)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(users -> onUserSuccess(users, command), this::handleError);

        mSubscriptions.add(mUserSubscription);

    }



    private void onUserSuccess(List<NewUser> users, Command command) {


        switch (command) {
            case SEARCH:
                ((PeopleAdapter) b.rvPeople.getAdapter()).getList().clear();
                KeyboardUtil.hideKeyBoard(b.etSearch,mActivity);
                break;
            case REFRESH:
                ((PeopleAdapter) b.rvPeople.getAdapter()).getList().clear();
                b.swipeToRefresh.setRefreshing(false);
                break;
            case PAGINATE:
                isLast = users.size() < 10;
                break;
        }


        ((PeopleAdapter) b.rvPeople.getAdapter()).getList().addAll(users);

        b.rvPeople.getAdapter().notifyDataSetChanged();


    }


    private void handleFollowing(boolean isFollowing, String userId) {


        Subscription subscription = (isFollowing ? RetrofitApi.getInstance().user().followUser(userId):
                                                   RetrofitApi.getInstance().user().unFollowUser(userId))
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFollowSuccess,this::handleError);
        mSubscriptions.add(subscription);


    }

    public void onFollowSuccess(BaseResponse response){

    }




    public  void handleError(Throwable t) {
       mActivity.handleError(t);
    }


    public void onDiscover(View view) {


        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Recolor())
                .addTransition(new Slide(Gravity.RIGHT))
                .addTransition(new Fade());

        TransitionManager.beginDelayedTransition( b.flListLayout, set);


        isDiscoverMode.set(true);
    }


    public void onNearby(View view) {


        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Recolor())
                .addTransition(new Slide(Gravity.LEFT))
                .addTransition(new Fade());

        TransitionManager.beginDelayedTransition( b.flListLayout, set);


        isDiscoverMode.set(false);

    }


    public void onTryAgain(View view) {
        BaseActivity.TriggerRefresh(new Bundle());
    }

    public void onFindClicked(View view) {
        mSearchAnimator.open(b.rvPeople.getWidth() - b.ivFind.getWidth());
        KeyboardUtil.showKeyBoard(b.etSearch, mActivity);


    }

    public void onCancelClicked(View view) {
        mSearchWord = "";
        b.etSearch.setText(mSearchWord);
        mSearchAnimator.close(b.rvPeople.getWidth() - b.ivFind.getWidth());
        getLoaderManager().restartLoader(GET_PEOPLE, null, this);
        KeyboardUtil.hideKeyBoard(b.etSearch, mActivity);

    }

    public void onMenuClicked(View view) {
        mActivity.onBackPressed();
    }





    private void writeToUser(String userId) {

        Subscription writeToUserSubscription = RetrofitApi
                .getInstance()
                .chat()
                .getUserChats(userId, true)
                .map(BaseResponse::getData)
                .map(response -> response.findChatIdByUserId(userId))
                .subscribe(chatId ->startChat(chatId,userId), this::handleError);

        mSubscriptions.add(writeToUserSubscription);
    }

    private void startChat(String chatId,String userId) {
        mActivity.replaceFragmentDown(ChatFragment.newInstance(chatId, userId));

    }

    @Override
    public void onFollow(NewUser newUser) {
        newUser.setIsFollowedByMe(!newUser.getIsFollowedByMe()) ;
        handleFollowing(newUser.getIsFollowedByMe(), newUser.getId());
    }

    @Override
    public void onChat(String userId) {

        writeToUser(userId);
    }

    @Override
    public void onEvent(String eventId) {

    }

    @Override
    public void onProfile(View avatar, NewUser user) {
        KeyboardUtil.hideKeyBoard(b.etSearch, mActivity);
        startFragment(avatar, user);
    }


    //cursor loader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] columns = MeshlyColumns.getColumns(NewUser.class);
        return new CursorLoader(mActivity, UserProvider.USERS_URI, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (isLoadAllowed) {
            isLoadAllowed = false;
            List<NewUser> list = ((PeopleAdapter) b.rvPeople.getAdapter()).getList();
            list.clear();
            data.moveToFirst();
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                list.add(mDao.getNewUser(data));
            }


            b.rvPeople.getAdapter().notifyDataSetChanged();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public enum Command {
        REFRESH, SEARCH, PAGINATE;
    }
}

