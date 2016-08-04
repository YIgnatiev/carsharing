package youdrive.today.network;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import youdrive.today.models.ApiCommand;
import youdrive.today.models.Car;
import youdrive.today.models.CreditCardModel;
import youdrive.today.models.CreditCardResponse;
import youdrive.today.models.InviteUser;
import youdrive.today.models.Invites;
import youdrive.today.models.LoginUser;
import youdrive.today.models.ReferralRules;
import youdrive.today.models.RegistrationUser;
import youdrive.today.models.SearchCar;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.PayoffResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.RegionsResponse;
import youdrive.today.response.RegistrationModel;
import youdrive.today.response.SearchCarResponse;
import youdrive.today.response.UserProfileResponse;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/3/15.
 */
public interface CarsharingService {

    @POST("/session")
    Observable<LoginResponse> login (@Body LoginUser user);

    @DELETE("/session")
    Observable<BaseResponse> logout();

    @GET("/user")
    Observable<UserProfileResponse> getUser();

    @POST("/payoff")
    Observable<PayoffResponse> payoff(@Body Object emptyBody);

    @GET("/status")
    Observable<CarResponse> getStatusCars(@Query("lat")double lat,@Query("lon")double lon);

    @GET("/status")
    Observable<CarResponse> getStatusCars();

    @GET("/info")

    Observable<PolygonResponse> getPolygon();

    @GET("/regions")
    Observable<RegionsResponse> getRegions();

    @POST("/invite")
    Observable<BaseResponse> invite(@Body InviteUser user);

    @POST("/order")
    Observable<CarResponse> booking(@Body Car car);

    @POST("/action")
    Observable <CommandResponse> command(@Body ApiCommand command) ;

    @DELETE("/order")
    Observable <CommandResponse> complete() ;

    @GET("/action/{token}")
    Observable <CommandResponse> result(@Path("token")String token) ;


    @POST("/create-account")
    Observable<RegistrationModel> createAccount(@Body RegistrationModel model);

    @PUT("/create-account/{registrationId}")
    Observable<RegistrationModel> updateAccount(@Path("registrationId") String registrationId,  @Body RegistrationUser user);

    @GET("/create-account/{registrationId}")
    Observable<RegistrationModel> createAccount(@Path("registrationId") String registrationId);



    @POST("/cards/init")
    Observable<CreditCardResponse> initCreditCard(@Body CreditCardModel model);


    @GET("/referral/rules")
    Observable<ReferralRules> getReferralRules();

    @POST("/referral/invites")
    Observable<BaseResponse> inviteUsersEmail(@Body Invites invites);

    @POST("/search")
    Observable<SearchCarResponse> createSearchCar(@Body SearchCar search);

    @GET("/search")
    Observable<SearchCarResponse> getSearchCar();

    @DELETE("/search")
    Observable<SearchCarResponse> deleteSearchCar();

}
