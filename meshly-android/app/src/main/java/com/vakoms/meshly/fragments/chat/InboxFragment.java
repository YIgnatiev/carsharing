package com.vakoms.meshly.fragments.chat;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.hidden.Recolor;
import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.InboxAdapter;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.InboxListener;
import com.vakoms.meshly.interfaces.MessageItemListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.chat.ChatResponseModel;
import com.vakoms.meshly.models.chat.MessageBody;
import com.vakoms.meshly.rest.RetrofitApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentInboxBinding;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 12/11/15.
 */

public class InboxFragment extends BaseFragment<MainActivity> implements InboxListener, MessageItemListener {


    private List<MessageBody> mChatsConnectionsList;
    private List<MessageBody> mChatsOthersList;
    private InboxAdapter mAdapterChatConnections;
    private InboxAdapter mAdapterChatOthers;


    private FragmentInboxBinding b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false);
            b.setListener(this);
            setupListView();
            b.rvChatListOthers.setVisibility(View.GONE);
            mActivity.setInboxListener(this);
            getAllChatList();
            addMenuListener(b.getRoot(),mActivity);

        }
        getAllChatList();
        return b.getRoot();
    }

    private void setupListView() {


        mChatsConnectionsList = new ArrayList<>();
        mChatsOthersList = new ArrayList<>();

        mAdapterChatConnections = new InboxAdapter(mActivity, mChatsConnectionsList, mActivity.mUser.getId(), this);
        mAdapterChatOthers = new InboxAdapter(mActivity, mChatsOthersList, mActivity.mUser.getId(), this);

        b.rvChatListConnections.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvChatListConnections.setAdapter(mAdapterChatConnections);

        b.rvChatListOthers.setLayoutManager(new LinearLayoutManager(mActivity));
        b.rvChatListOthers.setAdapter(mAdapterChatOthers);
    }

    public void getAllChatList() {
        mActivity.showProgress();
        mSubscriptions = new CompositeSubscription();
        Subscription subscription = RetrofitApi
                .getInstance()
                .chat()
                .getUserChats(mActivity.mUser.getId(), true)
                .retry(3)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(this::setChatList, this::handleError);
        mSubscriptions.add(subscription);
    }

    private void setChatList(ChatResponseModel model) {

        ArrayList<MessageBody> connectionsMsg = model.getConnections();
        ArrayList<MessageBody> othersMsg = model.getOthers();

        if (connectionsMsg != null) {
            mChatsConnectionsList.clear();
            mChatsConnectionsList.addAll(connectionsMsg);
            mAdapterChatConnections.notifyDataSetChanged();
        }

        if (othersMsg != null) {
            mChatsOthersList.clear();
            mChatsOthersList.addAll(othersMsg);
            mAdapterChatOthers.notifyDataSetChanged();
        }

        b.tvConnectionsCounter.setText(model.getUnreadConnections());
        b.tvOthersCounter.setText(model.getUnreadOthers());

        if (b.tvConnectionsCounter.getText().equals("0")) {
            b.tvConnectionsCounter.setVisibility(View.INVISIBLE);
        } else {
            b.tvConnectionsCounter.setVisibility(View.VISIBLE);
        }

        if (b.tvOthersCounter.getText().equals("0")) {
            b.tvOthersCounter.setVisibility(View.INVISIBLE);
        } else {
            b.tvOthersCounter.setVisibility(View.VISIBLE);
        }

        mActivity.stopProgress();
    }

    private void showChatsMessages() {

        b.tvNoChatsYet.setVisibility(mAdapterChatConnections.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);


        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Slide(Gravity.LEFT))
                .addTransition(new Fade());


        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }

        TransitionManager.beginDelayedTransition((ViewGroup) b.getRoot(), set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);


        b.tvConnections.setTextColor(Color.WHITE);
        b.tvOthers.setTextColor(getResources().getColor(R.color.white_transparent_50));


        b.rvChatListConnections.setVisibility(View.VISIBLE);
        b.rvChatListOthers.setVisibility(View.GONE);
    }

    private void showMessagesFromOthers() {

        b.tvNoChatsYet.setVisibility(mAdapterChatOthers.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);


        TransitionSet set = new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new Slide(Gravity.RIGHT))
                .addTransition(new Fade());


        if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            set.addTransition(new Recolor());
        }

        TransitionManager.beginDelayedTransition((ViewGroup) b.getRoot(), set);


        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(b.vwMovingBackGround.getMeasuredWidth(), b.vwMovingBackGround.getMeasuredHeight());
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        b.vwMovingBackGround.setLayoutParams(params);
        b.tvConnections.setTextColor(getResources().getColor(R.color.white_transparent_50));
        b.tvOthers.setTextColor(Color.WHITE);


        b.rvChatListOthers.setVisibility(View.VISIBLE);
        b.rvChatListConnections.setVisibility(View.GONE);

    }

    public void handleError(Throwable throwable) {
        mActivity.handleError(throwable);
    }

    private void onConfirmSuccess(Object o, MessageBody message) {
        message.getLastMessage().setNew(false);
        mActivity.replaceFragmentDown(ChatFragment.newInstance(message.getId(), message.getUser().getId()));


    }

    @Override
    public void onInboxUpdate() {
        getAllChatList();
    }

    //listeners
    public void onMenuClick(View view) {
        mActivity.openMainMenu();

    }

    public void onConnectoinsClick(View view) {
        showChatsMessages();

    }

    public void onOthersClick(View view) {
        showMessagesFromOthers();
    }


    @Override
    public void onUserClick(String userId) {
        mActivity.replaceFragmentDown(PeopleDetailFragment.getInstance(userId));


    }

    @Override
    public void onMessageClick(MessageBody messageBody) {

        if(messageBody!= null && messageBody.getLastMessage() != null ) messageBody.getLastMessage().setNew(false);

        Subscription subscription = RetrofitApi
                .getInstance()
                .chat()
                .confirmChat(messageBody.getId())
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .subscribe(response -> onConfirmSuccess(response, messageBody), this::handleError);

          mSubscriptions.add(subscription);

    }
}