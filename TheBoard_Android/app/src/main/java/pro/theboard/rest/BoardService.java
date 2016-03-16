package pro.theboard.rest;

import java.util.List;

import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;
import pro.theboard.models.retrofit.Customer;
import pro.theboard.models.retrofit.StatusResponse;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

import static pro.theboard.constants.Constants.CARD_VIEWED;
import static pro.theboard.constants.Constants.CUSTOMER;
import static pro.theboard.constants.Constants.DELETE_PROMO;
import static pro.theboard.constants.Constants.GET_CARDS;
import static pro.theboard.constants.Constants.GET_PROMO_CARDS;
import static pro.theboard.constants.Constants.REGISTER_DEVICE;

/**
 * Created by Oleh Makhobey on 16.07.2015.
 * tajcig@ya.ru
 */
public interface BoardService {

    @POST(GET_CARDS)
    Observable<List> sendAnswer(@Header("Authorization") String authorization ,
                    @Body Answer answer);

    @GET(GET_CARDS)
    Observable<List<Model>> getCards(@Header("Authorization") String authorization);

    @GET(GET_PROMO_CARDS)
    Observable<List<Model>> getPromoCards(@Header("Authorization") String authorization);

    @POST(DELETE_PROMO)
    Observable<Response> deletePromoCard(@Header("Authorization") String authorization , @Path("card_hash") String cardHash);

    @FormUrlEncoded
    @POST(CUSTOMER)
    Observable<Customer> getCustomer(
            @Field("device_type")String deviceType,
            @Field("device_id")String deviceId,
            @Field("language")String language,
            @Field("ip_addr")String ipAddr,
            @Field("latitude")String latitude,
            @Field("longitude")String longitude,
            @Field("location")boolean isLocationEnabled,
            @Field("notifications")boolean isNotificationEnabled);


    @FormUrlEncoded
    @POST(REGISTER_DEVICE)
    Observable<StatusResponse> sendToken(@Header("Authorization") String header,
                   @Field("device_token") String token);


    @POST(CARD_VIEWED)
    Observable<Response>sendAsViewed(@Header("Authorization") String authorization, @Path("card_hash") String cardHash);


}
