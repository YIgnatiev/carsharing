package com.vakoms.meshly.rest.services;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public interface EventService {



    @GET("/event/nearby")
    Observable<BaseResponse<List<Event>>> getEvents(@Query("lng") double longitude,
                                                    @Query("lat") double latitude);


    @GET("/event/userAttendingEvents/{userId}")
    Observable<BaseResponse<List<Event>>> getUserAttendingEvents(@Path("userId") String userId);


    @GET("/event/{eventId}/subscribe")
    Observable<BaseResponse> followEvent(@Path("eventId") String eventId,
                                         @Query("user") String userId );

    @GET("/event/{eventId}/unsubscribe")
    Observable <BaseResponse> unFollowEvent(@Path("eventId") String eventId,
                                            @Query("user") String userId);

    @GET("/event/{eventId}/details")
    Observable<BaseResponse<Event>> getEventById(@Path("eventId") String eventId );

    @GET("/event/{eventId}/subscribers")
    Observable<BaseResponse<List<NewUser>>> getEventSubscribers(@Path("eventId") String evendId);


    @POST("/suggestEvent")
    Observable<BaseResponse<Event>> sendEvent(@Body Event event);



}
