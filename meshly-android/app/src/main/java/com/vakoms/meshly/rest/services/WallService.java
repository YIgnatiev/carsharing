package com.vakoms.meshly.rest.services;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.wall.WallModel;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
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
public interface WallService {




    //depracated
    @GET("/wall/local")
    Observable<List<WallModel>> getLocalWallPosts(@Query("lng") double lng,
                                                  @Query("lat") double lat,
                                                  @Query("skip") int skip,
                                                  @Query("limit") int limit);
    //deprecated
    @GET("/wall/network")
    Observable<List<WallModel>> getNetworkWallPosts(@Query("skip") int skip,
                                                    @Query("limit") int limit);


     @GET("/wall/posts")
    Observable<BaseResponse<List<WallModel>>> getAllWallPosts(@Query("lng") double lng,
                                                              @Query("lat") double lat,
                                                              @Query("page") int page);


    @GET("/wall/active")
    Observable<BaseResponse<List<WallModel>>> getActiveWallPosts(@Query("skip") int skip,
                                                   @Query("limit") int limit);

    @GET("/wall/expired")
    Observable<BaseResponse<List<WallModel>>> getExpiredWallPosts(@Query("skip") int skip,
                                                    @Query("limit") int limit);


    @PUT("/wall/{wallId}")
    Observable<BaseResponse> updateWallPost(@Path("wallId") String wallId,
                                            @Body WallModel wallModel);
    @DELETE("/wall/{wallId}")
    Observable<BaseResponse> deleteWallPost(@Path("wallId") String wallId);

    @GET("/wall/{wallId}")
    Observable<BaseResponse<WallModel>> getWallPostById(@Path("wallId") String wallId);

    @POST("/wall")
    Observable<BaseResponse> createWallPost(@Body WallModel model);

    @POST("/wall/interested")
    Observable<BaseResponse> postInterestedWallPost(@Body WallModel model);



}
