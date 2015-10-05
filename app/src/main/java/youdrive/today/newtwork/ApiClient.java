package youdrive.today.newtwork;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import rx.Observable;
import youdrive.today.models.ApiCommand;
import youdrive.today.models.Car;
import youdrive.today.models.Command;
import youdrive.today.models.InviteUser;
import youdrive.today.models.LoginUser;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.RegionsResponse;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class ApiClient {

    private static String HOST = "https://youdrive.today";

    private CarsharingService mService;

    public ApiClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);
        client.interceptors().add(new AddCookiesInterceptor());
        client.interceptors().add(new ReceivedCookiesInterceptor());

        mService = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .setClient(new CustomClient(client))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(CarsharingService.class);
    }


    public Observable<LoginResponse> login(String email, String password) {
        return mService.login(new LoginUser(email, password));
    }

    public Observable<BaseResponse> logout() {
        return mService.logout();
    }

    public Observable<CarResponse> getStatusCars(double lat, double lon) {
        return mService.getStatusCars(lat, lon);
    }

    public Observable<CarResponse> getStatusCars() {
        return mService.getStatusCars();
    }

    public Observable<PolygonResponse> getPolygon() {
        return mService.getPolygon();
    }

    public Observable<RegionsResponse> getRegions() {
        return mService.getRegions();
    }

    public Observable<BaseResponse> invite(String email, Long phone, String region, boolean readyToUse) {
        return mService.invite(new InviteUser(email, phone, region, readyToUse));
    }

    public Observable<CarResponse> booking(String id, double lat, double lon) {
        return mService.booking(new Car(id, (float) lat, (float) lon));
    }

    public Observable<CommandResponse> command(Command command) {
        return mService.command(new ApiCommand(command.toString()));
    }

    public Observable<CommandResponse> complete() {
        return mService.complete();
    }

    public Observable<CommandResponse> result(String token) {
        return mService.result(token);
    }
}
