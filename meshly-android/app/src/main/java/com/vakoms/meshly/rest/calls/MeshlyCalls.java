package com.vakoms.meshly.rest.calls;

import android.location.Location;
import android.util.Base64;

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
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.rest.services.MeshlyService;
import com.vakoms.meshly.constants.Constants;

import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/12/15.
 */
public class MeshlyCalls extends BaseCalls {

    private MeshlyService mService;

    private RestAdapter mLocationAdapter;

    public MeshlyCalls(RestAdapter _adapter) {

        super(_adapter);
        mService = mAdapter.create(MeshlyService.class);
    }



    public Observable<Response> getUserLocation(double latitude, double longitude ){

        //"/extendedFindNearby?lat=%f&lng=%f&username=vipjoker&lang=en"

        MeshlyService service = RetrofitApi.getDynamicAdapter("http://api.geonames.org").create(MeshlyService.class);

        return service.getLocationAddress(latitude, longitude, "vipjoker", "en");
    }

    public Token refreshToken(String oldToken,String header)throws IOException{
        return mAdapter
                .create(MeshlyService.class)
                .refreshToken(new Token(oldToken, "refresh_token", Constants.CLIENT_ID, Constants.CLIENT_SECRET), header);

    }

    public Observable<Token> loginLocalUser(String email,String password){

        String toBase64 = String.format("%s:%s", email, password);
        String base64 = Base64.encodeToString(toBase64.getBytes(), Base64.DEFAULT);
        if(base64.contains("\n"))base64 =  base64.replace("\n","");
        String header = String.format("Basic %s", base64);
        return mService.loginLocalUser(new Token(null, "client_credentials", Constants.CLIENT_ID, Constants.CLIENT_SECRET), header);

    }

    public Observable<BaseResponse> forgotPassword(String email) {
        User user = new User();
        user.setEmail(email);
        return mService.forgotPassword(user);
    }

    public Observable<Token> loginWithFacebook(FacebookUser user ){
       return mService.loginWithFacebook(user);
    }

    public Observable<LinkedinData> fetchLinkedInData(String linkedInToken) {
        String url = "https://api.linkedin.com/v1/people";
        return RetrofitApi.getDynamicAdapter(url).create(MeshlyService.class).getLinkendInData(linkedInToken, "json");

    }

    public Observable<BaseResponse> checkEmail(String email){
        return mService.checkEmail(email);
    }

    public Observable<BaseResponse<List<Industry>>> getIndustries(){
        return mService.getIndustries();
    }

    public Observable<BaseResponse<UserMe>> registerUser(UserMe user){
        return mService.registerUser(user);
    }

    public Observable<BaseResponse<Version>> getCurrentStoreVersion(){
        return mService.getCurrentStoreVersion();
    }

    public Observable<LinkedInResponse> shareOnLinkedIn(String token ,LinkedInMassage message){

        String url = "https://api.linkedin.com/v1/people";
        return RetrofitApi.getDynamicAdapter(url).create(MeshlyService.class).shareOnLinkedId(token, "json", message);
    }


    public Observable<AddressResponse> getLocationAddress(Location location){
        if(mLocationAdapter == null)mLocationAdapter = RetrofitApi.getDynamicAdapter("http://nominatim.openstreetmap.org");

         return mLocationAdapter.create(MeshlyService.class).getAddress("en","json",10,"vipjoker@rambler.ru",1,location.getLatitude(),location.getLongitude());

    }












}
