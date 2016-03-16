package com.vakoms.meshly.rest.calls;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.services.EventService;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */




public class EventsCalls extends BaseCalls{


    private EventService mService;
    public EventsCalls (RestAdapter _adapter){
       super(_adapter);
        mService = mAdapter.create(EventService.class);
    }

    public Observable<BaseResponse<List<Event>>> getEvents(double longitude,double latitude){
        return mService.getEvents(longitude, latitude);
    }

    public  Observable<BaseResponse<List<Event>>> getAttedingEvents(String userId){
        return mService.getUserAttendingEvents(userId);
    }


    public Observable<BaseResponse<List<NewUser>>> getEventSubscribers(String eventId){
        return mService.getEventSubscribers(eventId);
    }

    public Observable <BaseResponse> followEvent(String eventId , String userId){
        return mService.followEvent(eventId, userId);
    }

    public Observable <BaseResponse> unfollowEvent(String eventId, String userId){
        return mService.unFollowEvent(eventId, userId);
    }

    public Observable <BaseResponse<Event>> getEventById(String eventId){
        return mService.getEventById(eventId);
    }

    public Observable<BaseResponse<Event>> sendEvent(Event event){
        return mService.sendEvent(event);
    }

}
