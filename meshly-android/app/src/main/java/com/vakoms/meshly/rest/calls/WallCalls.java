package com.vakoms.meshly.rest.calls;

import com.vakoms.meshly.models.BaseResponse;
import com.vakoms.meshly.models.Geo;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.rest.services.WallService;

import java.util.List;

import retrofit.RestAdapter;
import rx.Observable;

public class WallCalls extends BaseCalls {

    WallService mService;

    public WallCalls(RestAdapter _adapter) {
        super(_adapter);
        mService = mAdapter.create(WallService.class);
    }




    public Observable<BaseResponse<List<WallModel>>> getAllWallPosts(Geo _geoPoint,int page){
        return mService.getAllWallPosts(_geoPoint.getLng(),_geoPoint.getLat(),page);
    }


    public Observable<BaseResponse<List<WallModel>>> getActiveWallPosts(int _skip, int _limit) {

        return mService.getActiveWallPosts(_skip, _limit);
    }

    public Observable<BaseResponse<List<WallModel>>> getExpiredWallPosts(int _skip, int _limit) {

        return mService.getExpiredWallPosts(_skip, _limit);
    }



    public Observable<BaseResponse> updateWallPost(String _wallId, WallModel _model) {

        return mService.updateWallPost(_wallId, _model);
    }

    public Observable<BaseResponse<WallModel>> getWallPost(String _wallId) {

        return mService.getWallPostById(_wallId);
    }

    public Observable<BaseResponse> deleteWallPost(String _wallId) {

        return mService.deleteWallPost(_wallId);
    }

    public Observable<BaseResponse> interestedWallPost(WallModel _model) {

        return mService.postInterestedWallPost(_model);
    }

    public Observable<BaseResponse> createWallPost(WallModel _model) {

        return mService.createWallPost(_model);
    }


}
