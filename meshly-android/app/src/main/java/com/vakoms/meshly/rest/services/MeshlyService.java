package com.vakoms.meshly.rest.services;

import com.vakoms.meshly.models.AddressResponse;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.FacebookUser;
import com.vakoms.meshly.models.Industry;
import com.vakoms.meshly.models.LinkedInMassage;
import com.vakoms.meshly.models.LinkedInResponse;
import com.vakoms.meshly.models.LinkedinData;
import com.vakoms.meshly.models.Token;
import com.vakoms.meshly.models.User;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.models.Version;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/12/15.
 */
public interface MeshlyService {

    @GET("/industry")
    Observable<BaseResponse<List<Industry>>> getIndustries();



    @POST("/auth/facebook")
    Observable<Token>loginWithFacebook(@Body FacebookUser _user);

    @GET("/~:(id,email-address,positions,summary,firstName,lastName,skills,picture-urls::(original))")
    Observable<LinkedinData> getLinkendInData(@Query("oauth2_access_token") String token , @Query("format") String format);


    @GET("/extendedFindNearby")
    Observable<Response> getLocationAddress(
            @Path("lat") double latitude,
            @Path("lng") double longitude,
            @Path("username") String userName,
            @Path("lang") String language);



    @GET("/user/check")
    Observable<BaseResponse> checkEmail(@Query("email") String email );

    @GET("/version")
    Observable<BaseResponse<Version>> getCurrentStoreVersion();


    @POST("/~/shares")
    Observable<LinkedInResponse> shareOnLinkedId(@Query("oauth2_access_token")String tokenId,
                                                 @Query("format") String format ,
                                                 @Body LinkedInMassage message );


    @PUT("/password/forgot")
    Observable<BaseResponse> forgotPassword(@Body User user);

    @POST("/oauth/token")
    Token refreshToken(@Body Token token,@Header("Authorization") String authorization);

    @POST("/oauth/token")
    Observable<Token> loginLocalUser(@Body Token token ,@Header("Authorization") String authorization);


    @Headers({"Content-Type: application/json","UTF-8"})
    @POST("/register/local")
    Observable<BaseResponse<UserMe>> registerUser(@Body UserMe user);

//    @Headers("Accept-Language:en")
    @GET("/reverse")
    Observable<AddressResponse> getAddress(

                                            @Header("accept-language")String lanuguage,
                                            @Query("format") String format,
                                           @Query("zoom") int zoom,
                                           @Query("email") String email,
                                           @Query("addressdetails") int addressDetails,
                                           @Query("lat") double latitude,
                                           @Query("lon") double longitude );


    //http://nominatim.openstreetmap.org/reverse?format=xml&lat=52.5487429714954&lon=-1.81602098644987&zoom=18&addressdetails=1

}
