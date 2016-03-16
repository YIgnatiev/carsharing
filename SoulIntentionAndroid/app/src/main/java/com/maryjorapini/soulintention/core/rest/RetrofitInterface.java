package com.maryjorapini.soulintention.core.rest;

import com.maryjorapini.soulintention.core.APIConstants;
import com.maryjorapini.soulintention.core.BODY_DELETE;
import com.maryjorapini.soulintention.data_models.response.FavouriteIdResponseModel;
import com.maryjorapini.soulintention.data_models.response.Post.AboutAuthorModel;
import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;
import com.maryjorapini.soulintention.global.Constants;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Sasha on 19.11.2014.
 */
public interface RetrofitInterface {
    @FormUrlEncoded
    @POST(APIConstants.URL_START_MOBILE)
    void startMobile(@Field(Constants.PARAM_DEVICE_ID) String mobileId,
                     Callback<SessionMobileResponseModel> callback);

    @GET(APIConstants.URL_END_MOBILE)
    void endMobile( Callback<SessionMobileResponseModel> callback);

    @GET(APIConstants.URL_POST)
    void getPost(@Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                 Callback<List<PostResponseModel>> callback);

    @GET(APIConstants.URL_POST)
    void getPost(@Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                 @Query(Constants.PARAM_ORDER_BY) String rate,
                 Callback<List<PostResponseModel>> callback);

    @GET(APIConstants.API_ABOUT)
    void getAbout(Callback<List<AboutAuthorModel>> callback);

    @GET(APIConstants.URL_FAVOURITE)
    void getFavourites(@Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                       Callback<List<PostResponseModel>> callback);
    @GET(APIConstants.URL_FAVOURITE)
    void getFavourites(@Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                       @Query(Constants.PARAM_ORDER_BY) String rate,
                       Callback<List<PostResponseModel>> callback);
    @GET(APIConstants.URL_FAVOURITE)
    void getFavouritesSearch(@Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                       @Query(Constants.PARAM_TITLE) String title,
                       Callback<List<PostResponseModel>> callback);
    @FormUrlEncoded
    @POST(APIConstants.URL_FAVOURITE)
    void postFavourite(@Field(Constants.PARAM_POST_ID) int postId,
                     Callback<SessionMobileResponseModel> callback);

    @BODY_DELETE(APIConstants.URL_FAVOURITE + "/{id}")
    void deleteFavourite(@Path("id") int postId,
                       Callback<SessionMobileResponseModel> callback);

    @GET(APIConstants.URl_FAVOURITE_ID)
    void getFavoriteId(Callback<List<FavouriteIdResponseModel>> callback);

    @GET(APIConstants.API_SEARCH)
    void getSearch(@Query(Constants.PARAM_TITLE) String title, @Query(Constants.PARAM_LIMIT) int limit, @Query(Constants.PARAM_OFFSET) int offset,
                 Callback<List<PostResponseModel>> callback);

    @FormUrlEncoded
    @POST(APIConstants.API_RATE)
    void postRate(@Field(Constants.PARAM_POST_ID) int postId, @Field(Constants.PARAM_RATE) int rate,
                       Callback<SessionMobileResponseModel> callback);
    @FormUrlEncoded
    @POST(APIConstants.API_DEVICE_TOKEN)
    void deviceToken(@Field(Constants.PARAM_DEVICE_TOKEN) String postId, @Field(Constants.PARAM_DEVICE_OS) String OS,
                  Callback<SessionMobileResponseModel> callback);

}
