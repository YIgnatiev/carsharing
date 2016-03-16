package pro.theboard.rest;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pro.theboard.constants.Constants;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;
import pro.theboard.models.retrofit.Customer;
import pro.theboard.models.retrofit.StatusResponse;
import pro.theboard.utils.Preferences;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class RetrofitApi {

    private static RetrofitApi INSTANCE;


    private static BoardService mService;
    private Context mContext;

    public static RetrofitApi getInstance(Context _context) {
        if (INSTANCE == null) INSTANCE = new RetrofitApi(_context);
        return INSTANCE;
    }

    public RetrofitApi(Context _context) {
        this.mContext = _context;

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setRequestInterceptor(new RequestHeaders())
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mService = adapter.create(BoardService.class);
    }

    public Observable<Customer> login(String latitude, String longitude, boolean isLocationEnabled,
                                      boolean isNotificationEnabled) {

        return mService.getCustomer(
                Constants.TYPE_ANDROID,
                getDeviceId(),
                getLanguage(),
                getIp(),
                latitude,
                longitude,
                isLocationEnabled,
                isNotificationEnabled)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<Model>> getCards() {
        return mService.getCards("CUSTOMER " + Preferences.getCustomerHash())
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<Model>> getPromoCards(){
        return mService.getPromoCards("CUSTOMER " + Preferences.getCustomerHash())
                .timeout(3,TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Response> deletePromocard(String cardHash){
       return mService.deletePromoCard("CUSTOMER " + Preferences.getCustomerHash() ,cardHash)
                .timeout(3,TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<List> sendAnswer(final Answer _answer) {
        return mService.sendAnswer("CUSTOMER " + Preferences.getCustomerHash(), _answer)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }




    public Observable<StatusResponse> sendGcmToken(String token) {
        return mService
                .sendToken("CUSTOMER " + Preferences.getCustomerHash(), token)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Response> sendAsViewed(String cardHash) {
        return mService
                .sendAsViewed("CUSTOMER " + Preferences.getCustomerHash(), cardHash)
                .timeout(3, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private String getIp() {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }


    private String getDeviceId() {
        return Settings
                .Secure
                .getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

    }

    private String getLanguage() {
        return Locale.getDefault().getCountry();
    }


}
//    public void getUserDetailsForReceivedTokens(Token token) {
//        showProgress();
//        mSubscription =
//                handleTokens(token)
//                        .map(userMe-> userMe.getData().getLinkedin().getToken())
//                        .flatMap(linkedInToken -> RetrofitApi.getInstance().meshly().fetchLinkedInData(linkedInToken))
//                        .timeout(3, TimeUnit.SECONDS)
//                        .map(this::fromUserLinkedInToUserMe)
//                        .flatMap(user -> RetrofitApi.getInstance().user().updateUser(user))
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(this::onLoginSuccess, this::handleError);
//
//    }
//}
