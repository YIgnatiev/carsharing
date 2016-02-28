package com.vakoms.meshly.rest.calls;

import retrofit.RestAdapter;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/11/15.
 */
public abstract class BaseCalls {
    protected RestAdapter mAdapter;

    protected BaseCalls(RestAdapter _adapter){
        mAdapter = _adapter;
    }
//
//    public  static <T>Observable<T>  getObservableRequest() {
//        return Observable.<T>empty()
//                .retry(3)
//                .timeout(3, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
