package youdrive.today.login;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import youdrive.today.response.BaseResponse;

public class BaseObservable {

    public static Observable<BaseResponse> ApiCall(final RequestListener listener) {
        return Observable
                .create(new Observable.OnSubscribe<BaseResponse>() {
                    @Override
                    public void call(Subscriber<? super BaseResponse> subscriber) {
                        BaseResponse request = listener.onRequest();
                        if (request != null) {
                            subscriber.onNext(request);
                        } else {
                            subscriber.onError(new IOException("Request is null"));
                        }

                        subscriber.onCompleted();
                    }
                })
                .timeout(5, TimeUnit.SECONDS)
                .retry(3)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
