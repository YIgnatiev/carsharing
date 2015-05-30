package youdrive.today;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import youdrive.today.login.RequestListener;
import youdrive.today.response.BaseResponse;

public class BaseObservable {

    public static Observable<BaseResponse> ApiCall(final RequestListener listener) {
        return Observable
                .create(new Observable.OnSubscribe<BaseResponse>() {
                    @Override
                    public void call(Subscriber<? super BaseResponse> subscriber) {
                        subscriber.onNext(listener.onRequest());
                        subscriber.onCompleted();
                    }
                })
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BaseResponse> ApiIntervalCall(final RequestListener listener, int interval){
        return Observable
                .interval(interval, TimeUnit.SECONDS)
                .map(new Func1<Long, BaseResponse>() {
                    @Override
                    public BaseResponse call(Long aLong) {
                        return listener.onRequest();
                    }
                })
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
