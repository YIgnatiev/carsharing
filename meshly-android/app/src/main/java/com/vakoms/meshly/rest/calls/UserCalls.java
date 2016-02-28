package com.vakoms.meshly.rest.calls;

import android.location.Location;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.CurrentCity;
import com.vakoms.meshly.models.Following;
import com.vakoms.meshly.models.Geo;
import com.vakoms.meshly.models.HasSeenWelcomeModel;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.Privacy;
import com.vakoms.meshly.models.UserMe;
import com.vakoms.meshly.rest.services.UserService;
import com.vakoms.meshly.constants.Constants;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.Body;
import rx.Observable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public class UserCalls extends BaseCalls {

    private UserService mService;

    public UserCalls(RestAdapter _adapter) {

        super(_adapter);
        mService = mAdapter.create(UserService.class);
    }

    public Observable<BaseResponse> followUser(String userId) {
        return mService.followUser(userId, Constants.FOLLOW);
    }

    public Observable<BaseResponse> unFollowUser(String userId) {
        return mService.followUser(userId, Constants.UNFOLLOW);


    }

    public Observable<BaseResponse<List<NewUser>>> getUsers(String keyWord, double latitude,  double longitude,int page) {
        if (keyWord == null) keyWord = "";
        return mService.getUsers(keyWord, longitude, latitude, page);
    }

    public Observable<BaseResponse<List<NewUser>>> getDiscoverUsers() {
        return mService.getDiscoverUsers();
    }

    public Observable <BaseResponse<UserMe>> getRegisteredUserDetails() {
        return mService.getRegisteredUserDetails();

    }

    public Observable<BaseResponse> removeUser(String userId) {
       return mService.removeUser(userId);
    }

    public Observable<BaseResponse> logoutUser(String userId) {
        return mService.logout(userId);
    }

    public Observable<UserMe> updateUser(UserMe user) {
        return mService.updateUser(user);

    }

    public Observable<UserMe> uploadUserImage(UserMe user)  {

        return mService.uploadUserImage(user);

    }

    public Observable<BaseResponse<UserMe>> updateUserPicture(UserMe user) {
       return mService.updateUserPictureUrl(true, user);
    }

    public Observable<BaseResponse<List<NewUser>>> getUserWithExtraData(String userId){
        return mService.getUserByIdWithExtra(userId);
    }

    public Observable<BaseResponse<Following>> getUserFollowing(){
        return mService.getUserFollowing();
    }

    public Observable<BaseResponse> updatePrivacy(Privacy privacy){
        return mService.updatePrivacy(privacy);
    }

    public Observable<BaseResponse> updateUserLocation(Location location){
        Geo geo = new Geo(location.getLatitude(),location.getLongitude());
        return mService.updateUserLocation(geo);
    }

    public Observable<BaseResponse<List<NewUser>>> getTopUsers(Location location){
        return mService.getTopUsers(location.getLatitude(), location.getLongitude());
    }



    public Observable<BaseResponse<HasSeenWelcomeModel<Boolean>>> saveHasSeen(HasSeenWelcomeModel<Integer> body){
        return mService.saveHasSeen(body);
    }

    public Observable<BaseResponse<HasSeenWelcomeModel<Boolean>>> getHasWellcomeScreenNeeded(){
        return mService.getHasWellcomeScreenNeeded();
    }

    public Observable<BaseResponse> saveHomeCity(@Body CurrentCity body){
        return mService.saveHomeCity(body);
    }
    public Observable<BaseResponse> updateAndroidGcmToken(NewUser user){
        return mService.updateAndroidGcmToken(user);
    }
}
