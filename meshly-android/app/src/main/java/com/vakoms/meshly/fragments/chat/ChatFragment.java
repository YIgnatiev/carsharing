package com.vakoms.meshly.fragments.chat;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.adapters.ChatAdapter;
import com.vakoms.meshly.fragments.BaseFragment;
import com.vakoms.meshly.fragments.people.PeopleDetailFragment;
import com.vakoms.meshly.interfaces.AddMessageListener;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Message;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.views.ResideMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.FragmentChatBinding;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Oleh Makhobey on 11.05.2015.
 * tajcig@ya.ru
 */
public class ChatFragment extends BaseFragment<MainActivity> implements AddMessageListener, View.OnFocusChangeListener {




    public static final String CHAT_ID = "chat_id";
    public static final String USER_ID = "userToChatWith";
    public static final String IS_CONFIRMED = "is_confirmed";
    public static final String IS_PROTECTED_KEY = "is_protected";





    private Location mLocation;
    private String instantMessage;

    private ChatAdapter mAdapter;
    private String chatId;
    private String mMessage;
    private List<Message> list = new ArrayList<>();
    private NewUser userToChatWith;
    private boolean isUserProtected;




    private FragmentChatBinding b;

    public static ChatFragment newInstance(String chatId, String userId) {
        ChatFragment frag = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHAT_ID, chatId);
        bundle.putString(USER_ID, userId);
        bundle.putBoolean(IS_CONFIRMED, !chatId.equals(""));
        bundle.putBoolean(IS_PROTECTED_KEY, false);
        frag.setArguments(bundle);
        return frag;
    }


    public static ChatFragment newInstance(String chatId, String userId, String _message) {
        ChatFragment frag = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHAT_ID, chatId);
        bundle.putString(USER_ID, userId);
        bundle.putBoolean(IS_CONFIRMED, !chatId.equals(""));
        bundle.putBoolean(IS_PROTECTED_KEY, false);
        frag.setInstantMessage(_message);
        frag.setArguments(bundle);
        return frag;
    }



    public void setInstantMessage(String _message) {
        this.instantMessage = _message;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setAddMessageistener(this);
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.setAddMessageistener(null);
        mActivity.getResideMenu().getSwipeDirections().clear();
        mActivity.getResideMenu().setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        KeyboardUtil.hideKeyBoard(b.etMessage, mActivity);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(b == null) {
            b = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
            b.setListener(this);
            setListeners();
            setFields();
            setUpListView();
            if (instantMessage != null) {
                createMessage(instantMessage);
                instantMessage = null;
            }

            if (!chatId.equals("")) getChat(chatId);
        }

        return b.getRoot();
    }


    private void setListeners() {

        b.etMessage.setOnFocusChangeListener(this);
    }

    private void setUpListView() {

        mAdapter = new ChatAdapter(getActivity(), list);
        b.lvList.setAdapter(mAdapter);
    }

    private void setFields() {
        chatId = getArguments().getString(CHAT_ID, "");
        isUserProtected = getArguments().getBoolean(IS_PROTECTED_KEY, false);
        mLocation = P.GPS.getLocation();
        if (isUserProtected) {
            b.tvProtectedUser.setVisibility(View.VISIBLE);
        } else {
            b.tvProtectedUser.setVisibility(View.GONE);
        }

        fillUserData();
    }

    /**
     * Scrolls msg list to the end in case user already at the end of the msg list and tapped on
     * EditText. Keyboard will shift msg list, and msg list will scroll to the end.
     */
    private void scrollListToTheEnd() {

        //are we at the end of msg list?
        if (b.lvList.getCount() - b.lvList.getFirstVisiblePosition() <= 10) {
            b.lvList.smoothScrollToPosition(b.lvList.getCount());

        }
    }


    private void sendMessage(String strMessage){
        Message message = new Message();
        message.setChat_id(chatId);
        message.setUser_id(mActivity.mUser.getId());
        message.setMessage(strMessage);
        sendMessageViaNetwork(message);
    }

    private void sendMessage(String strMessage, String chatid) {
        Message message = new Message();
        message.setChat_id(chatid);
        message.setUser_id(mActivity.mUser.getId());
        message.setMessage(strMessage);
        sendMessageViaNetwork(message);
    }
    private void sendMessageViaNetwork(Message message) {




        Subscription subscription =
                RetrofitApi.getInstance()
                        .chat()
                        .addMessage(message)
                        .timeout(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onMessageSuccess, this::handleError);

        mSubscriptions.add(subscription);

    }

    protected void handleError(Throwable throwable) {
        mActivity.handleError(throwable);

    }


    private void onMessageSuccess(Message message) {
        mActivity.stopProgress();
        list.add(message);
        mAdapter.notifyDataSetChanged();
        b.lvList.smoothScrollToPosition(list.size() - 1);
    }



    public void createChat(final String message) {
        mMessage = message;
        mActivity.showProgress();
        Subscription subscription = RetrofitApi
                .getInstance()
                .chat()
                .createChat(mActivity.mUser.getId(), getArguments().getString(USER_ID))
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Message::getChat_id)
                .subscribe(chat_id -> sendMessage(mMessage, chat_id), this::handleError);

        mSubscriptions.add(subscription);

    }

    private void fillUserData() {
        String userId = getArguments().getString(USER_ID);

        Subscription subscription = RetrofitApi
                .getInstance().user()
                .getUserWithExtraData(userId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BaseResponse::getData)
                .map(list-> list.get(0))
                .subscribe(this::onUserSuccess,this::handleError);

        mSubscriptions.add(subscription);
    }

    private void  onUserSuccess(NewUser user) {

        userToChatWith = user;
        b.tvUserName.setText(userToChatWith.getUsername());
    }



    public void getChat(String chatId) {
        mActivity.showProgress();

       Subscription subscription = RetrofitApi.getInstance()
                .chat()
                .getChatMessagesById(chatId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetChatSuccess, this::handleError);

        mSubscriptions.add(subscription);


    }


    private  void onGetChatSuccess(List<Message> messages){

            mActivity.stopProgress();

            list.clear();
            list.addAll(messages);
            if (list.size() > 0) {
                mAdapter.notifyDataSetChanged();
                b.lvList.setSelection(list.size() - 1);
                if (!list.get(list.size() - 1).getUser_id().equals(mActivity.mUser.getId()))
                    readMessage(list.get(list.size() - 1).getId());
                mActivity.checkNewMessages(mActivity.mUser);
            }

    }



    private void readMessage(String messageId) {


        Subscription subscription = RetrofitApi
                .getInstance()
                .chat()
                .readMessage(new Message(),messageId)
                .timeout(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        mSubscriptions.add(subscription);
    }


    private void createMessage(String message) {

        if (!message.equals("")) {
            if (getArguments().getBoolean(IS_CONFIRMED)) {
                sendMessage(message);
            } else {
                createChat(message);
            }
        } else {
            Toast.makeText(getActivity(), "Message is empty", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void addMessage(String user, String timestamp, String username, String message, String chatId, String messageId) {
        if (chatId.equals(this.chatId)) {
            Message item = new Message();
            item.setUser_id(user);
            item.setTimestamp(timestamp);
            item.setUsername(username);
            item.setMessage(message);
            item.setChat_id(chatId);
            item.setId(messageId);
            list.add(item);
            mAdapter.notifyDataSetChanged();
            b.lvList.smoothScrollToPosition(list.size() - 1);
        }
    }

    // listeners


    public void onUserName(View view){
        mActivity.replaceFragmentLeft(PeopleDetailFragment.getInstance(userToChatWith.getId()));

    }


    public void onMessage(View view){
        scrollListToTheEnd();

    }

    public void onSend(View view){

        String message = b.etMessage.getText().toString();
        b.etMessage.setText("");
        createMessage(message);
    }

    public void onBack(View view){
        mActivity.onBackPressed();
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        scrollListToTheEnd();
    }




}