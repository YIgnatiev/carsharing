package youdrive.today.login;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by psuhoterin on 26.04.15.
 */
public class BaseObservable {


    public static Observable<String> ApiObservable(final RequestListener listener) {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String request = listener.onRequest();
                        if (request != null) {
                            subscriber.onNext(request);
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
