package com.vakoms.meshly.rest.calls;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Message;
import com.vakoms.meshly.models.chat.ChatResponseModel;
import com.vakoms.meshly.rest.services.ChatService;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public class ChatCalls extends BaseCalls {


    private ChatService mService;
    public ChatCalls(RestAdapter _adapter) {
        super(_adapter);
        mService = mAdapter.create(ChatService.class);
    }

    public Observable<BaseResponse<ChatResponseModel>> getUserChats(String userId,boolean isNew){
       return mService.getUserChats(userId, isNew);
    }

    public Observable<BaseResponse> readMessage(Message massage , String messageId){
        return mService.readMessage(massage, messageId);
    }


    public Observable<Message> createChat(String myId, String toId){
        return mService.createChat(myId, toId);


    }

    public Observable<List<Message>> getChatMessagesById(String chatId){
        return mService.getChatMessagesById(chatId);
    }

    public Observable<BaseResponse> confirmChat(String chatId){
        return mService.confirmChat(chatId);
    }

    public Observable<Message> addMessage(Message message){
        return mService.addMessage(message);
    }


}
