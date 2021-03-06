package youdrive.today.network;

import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import youdrive.today.BuildConfig;
import youdrive.today.models.ApiCommand;
import youdrive.today.models.Car;
import youdrive.today.models.Command;
import youdrive.today.models.CreditCardModel;
import youdrive.today.models.CreditCardResponse;
import youdrive.today.models.InviteUser;
import youdrive.today.models.Invites;
import youdrive.today.models.LoginUser;
import youdrive.today.models.ReferralRules;
import youdrive.today.models.RegisterDevice;
import youdrive.today.models.RegistrationUser;
import youdrive.today.models.SearchCar;
import youdrive.today.models.TestInfoPush;
import youdrive.today.models.TestPush;
import youdrive.today.response.BaseResponse;
import youdrive.today.response.CarResponse;
import youdrive.today.response.CommandResponse;
import youdrive.today.response.LoginResponse;
import youdrive.today.response.MessagesResponse;
import youdrive.today.response.PayoffResponse;
import youdrive.today.response.PolygonResponse;
import youdrive.today.response.RegionsResponse;
import youdrive.today.response.RegistrationModel;
import youdrive.today.response.SearchCarResponse;
import youdrive.today.response.UploadCareResponse;
import youdrive.today.response.UploadGroupResponse;
import youdrive.today.response.UserProfileResponse;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class ApiClient {

    private static String HOST = "https://youdrive.today";

//    private static String HOST = "http://188.166.36.215:3000";

    private static String UPLOADCARE_KEY = "507278759b3577e5f137";
    private static final boolean enableLog = BuildConfig.DEBUG;
    private CarsharingService mService;
    private UploadService mUploadService;

    public ApiClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.interceptors().add(new AddCookiesInterceptor());
        client.interceptors().add(new ReceivedCookiesInterceptor());

        mService = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .setClient(new CustomClient(client))
                .setLogLevel(enableLog ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build()
                .create(CarsharingService.class);

        mUploadService = new RestAdapter.Builder()
                .setEndpoint("https://upload.uploadcare.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(UploadService.class);
    }

    public Observable<LoginResponse> login(String email, String password) {
        return mService.login(new LoginUser(email, password));
    }

    public Observable<BaseResponse> logout() {
        return mService.logout();
    }

    public Observable<UserProfileResponse> getUserProfile() {
        return mService.getUser();
    }
    public Observable<PayoffResponse> payoff() {
        return mService.payoff(new Object());
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

    public Observable<RegistrationModel> createUser() {
        return mService
                .createAccount(new RegistrationModel())
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RegistrationModel> updateUser(String userId, RegistrationUser user) {
        return mService
                .updateAccount(userId, user)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RegistrationModel> createUser(String userId) {
        return mService
                .createAccount(userId)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CreditCardResponse> initCard(CreditCardModel model) {
        return mService.initCreditCard(model)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> inviteUsersEmail(Invites inviteList) {
        return mService.inviteUsersEmail(inviteList)
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ReferralRules> getReferralRules() {
        return mService.getReferralRules()
                .retry(3)
                .timeout(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UploadCareResponse> uploadFile(File file) {
        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        return mUploadService.uploadFile(UPLOADCARE_KEY, 1, typedFile);
    }

    public Observable<UploadGroupResponse> uploadGroup(Map<String, String> params) {
        return mUploadService.uploadGroup(UPLOADCARE_KEY, params);
    }

    public Observable<SearchCarResponse> postSearchCar(double lat, double lon, double radius) {
        return mService.createSearchCar(new SearchCar((float) lat, (float) lon, (int) radius));
    }

    public Observable<SearchCarResponse> getSearchCar() {
        return mService.getSearchCar();
    }
    public Observable<SearchCarResponse> deleteSearchCar() {
        return mService.deleteSearchCar();
    }

    public Observable<BaseResponse> postRegisterDevice(String os, String token, String os_version, String device_type) {
        return mService.registerWithDevice(new RegisterDevice(os, token, os_version, device_type));
    }

    public Observable<BaseResponse> postTestPush(){
        return mService.postWithTestPush(new TestPush());
    }
    public Observable<BaseResponse> getTestInfoPush(){
        return mService.postWithInfoPush();
    }

    public Observable<MessagesResponse> getMesssage() {
        return mService.getMessages();
    }

}
