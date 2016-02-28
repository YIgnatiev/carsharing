package com.vakoms.meshly.rest.services;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.CurrentCity;
import com.vakoms.meshly.models.Following;
import com.vakoms.meshly.models.Geo;
import com.vakoms.meshly.models.HasSeenWelcomeModel;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.Privacy;
import com.vakoms.meshly.models.UserMe;

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
public interface UserService {

    @PUT("/user")
    Observable<BaseResponse> updateAndroidGcmToken(@Body NewUser user);

    @GET("/user/{userId}/{follow}")
    Observable<BaseResponse> followUser(@Path("userId") String userId,
                            @Path("follow") String follow);


    @GET("/user/search/{path}")
    Observable<BaseResponse<List<NewUser>>> getUsers(
            @Path("path") String keyword,
            @Query("lng") double longitude,
            @Query("lat") double latitude,
            @Query("page") int page);

    @GET("/user/discoverSearch")
    Observable<BaseResponse<List<NewUser>>> getDiscoverUsers();

    @GET("/user/withExtraData/{userId}")
    Observable<BaseResponse<List<NewUser>>> getUserByIdWithExtra(@Path("userId") String userId);

    //userMe
    @PUT("/user")
    Observable<UserMe> updateUser(@Body UserMe user);

    @PUT("/user")
    Observable<UserMe> uploadUserImage(@Body UserMe user);

    @PUT("/user")
    Observable<BaseResponse<UserMe>> updateUserPictureUrl(@Query("override") boolean isOverrided, @Body UserMe user);

    @GET("/user/remove/{userId}")
    Observable<BaseResponse> removeUser(@Path("userId") String userId);

    @GET("/user/logout/{userId}?device=android")
    Observable<BaseResponse> logout(@Path("userId") String userId);

    @PUT("/user/location")
    Observable<BaseResponse> updateUserLocation(@Body Geo geo);

    @GET("/user/details")
    Observable <BaseResponse<UserMe>> getRegisteredUserDetails();

    @GET("/user/following")
    Observable<BaseResponse<Following>> getUserFollowing();

    @PUT("/privacy")
    Observable<BaseResponse>updatePrivacy(@Body Privacy privacy);

    @GET("/user/topUsers")
    Observable<BaseResponse<List<NewUser>>> getTopUsers(@Query("lng") double longitude, @Query("lat") double latitude);

    @POST("/onboard/hasSeen")
    Observable<BaseResponse<HasSeenWelcomeModel<Boolean>>> saveHasSeen(@Body HasSeenWelcomeModel<Integer> body);

    @GET("/onboard/hasSeen")
    Observable<BaseResponse<HasSeenWelcomeModel<Boolean>>> getHasWellcomeScreenNeeded();

    @POST("/user/currentCity")
    Observable<BaseResponse> saveHomeCity(@Body CurrentCity body);



}
