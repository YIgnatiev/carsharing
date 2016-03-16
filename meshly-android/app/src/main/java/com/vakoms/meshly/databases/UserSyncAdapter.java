package com.vakoms.meshly.databases;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Event;
import com.vakoms.meshly.models.Geo;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.ReactiveLocation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */
public class UserSyncAdapter extends AbstractThreadedSyncAdapter {

    private final ContentResolver mContentResolver;
    private ReactiveLocation mRectiveLocation;
    private AccountManager mAccountManager;
    private final static UserDAO mTodoDAO = UserDAO.getInstance();


    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    public UserSyncAdapter(Context context, boolean b) {
        super(context, b);
        Log.i("syncadapter", "adapter created");
        mContentResolver = context.getContentResolver();
        mRectiveLocation = ReactiveLocation.getInstance(context);
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        if (P.GPS.getLongitude() == 0 || P.GPS.getLatitude() == 0)
            mRectiveLocation.getFastLocation();

        getUsers(1, "");
        getWall(P.GPS.getGeo(), 1);
        getEvents();

    }


    public void getWall(Geo geo, int page) {
        Subscription subscription = RetrofitApi
                .getInstance()
                .wall()
                .getAllWallPosts(geo, page)
                .retry(2)
                .timeout(3, TimeUnit.SECONDS)
                .map(BaseResponse::getData)
                .subscribe(this::saveWalls, this::handleException);

        mSubscriptions.add(subscription);
    }

    public void saveWalls(List<WallModel> wallModels) {
        try {
            mTodoDAO.saveWall(mContentResolver, wallModels);
        } catch (Exception e) {
            Log.v("sync", "can't save walls" + e.getMessage());

        }
    }

    public void getEvents() {
        Subscription subscription = RetrofitApi
                .getInstance()
                .events()
                .getEvents(P.GPS.getLatitude(), P.GPS.getLongitude())
                .retry(2)
                .timeout(3, TimeUnit.SECONDS)
                .map(BaseResponse::getData)
                .subscribe(this::saveEvents, this::handleException);

        mSubscriptions.add(subscription);
    }

    public void saveEvents(List<Event> events) {
        try {
            mTodoDAO.saveEvents(mContentResolver, events);
        } catch (Exception e) {
            Log.v("sync", "can't save events" + e.getMessage());

        }
    }

    private void getUsers(int page, String keyword) {


        Subscription subscription = RetrofitApi
                .getInstance()
                .user()
                .getUsers(keyword, P.GPS.getLatitude(), P.GPS.getLongitude(), page)
                .retry(2)
                .timeout(3, TimeUnit.SECONDS)
                .map(BaseResponse::getData)
                .subscribe(this::onUserSuccess, this::handleException);

        mSubscriptions.add(subscription);
    }

    private void onUserSuccess(List<NewUser> users) {
        try {
            mContentResolver.delete(UserProvider.USERS_URI, null, null);
            mContentResolver.notifyChange(UserProvider.USERS_URI, null, false);


            mTodoDAO.addUsers(mContentResolver, users);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void handleException(Throwable throwable) {
        try {
            RetrofitError error = (RetrofitError) throwable;
            TypedByteArray byteArray = (TypedByteArray) error.getResponse().getBody();
            String message = new String(byteArray.getBytes());
            BaseResponse response = new Gson().fromJson(message, BaseResponse.class);
            int  status = response.getStatus();
            if(status == 403){
                P.saveIsLoggedWithAnotherDevice(true);

            }
        }catch (Exception ex){

            Log.v("sync" , "unknow error " + ex.getMessage());
        }

    }

}
