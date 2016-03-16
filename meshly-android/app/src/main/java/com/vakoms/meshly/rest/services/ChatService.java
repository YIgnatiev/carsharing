package com.vakoms.meshly.rest.services;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Message;
import com.vakoms.meshly.models.chat.ChatResponseModel;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public interface ChatService {



    @GET("/chat/ext/{path}")
    Observable<BaseResponse<ChatResponseModel>> getUserChats(@Path("path") String userId,
                                               @Query("new") boolean isNew );
    @PUT("/message/{messageId}")
    Observable<BaseResponse> readMessage(@Body Message message,@Path("messageId") String messageId);

    @GET("/user/connect")
    Observable<Message> createChat(@Query("id")String myId,@Query("to")String toId);

    @GET("/message/get")
    Observable<List<Message>> getChatMessagesById(@Query("chat_id") String chatId);


    @GET("/chat/confirm/{chatId}")
    Observable<BaseResponse> confirmChat(@Path("chatId") String chatId);

    @POST("/message")
    Observable<Message> addMessage(@Body Message message);

}
