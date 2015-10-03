package youdrive.today.newtwork;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import youdrive.today.models.ApiCommand;
import youdrive.today.models.Car;
import youdrive.today.models.InviteUser;
import youdrive.today.models.LoginUser;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.RegionsResponse;

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

}
