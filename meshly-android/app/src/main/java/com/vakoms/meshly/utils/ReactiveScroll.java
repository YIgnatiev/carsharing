package com.vakoms.meshly.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/14/15.
 */
public class ReactiveScroll {


    private  RecyclerView.OnScrollListener mListener;





    public  Observable<Integer>  observeScroll(RecyclerView recyclerView){
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {


                mListener = new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int lastitem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        subscriber.onNext(lastitem);


                    }
                };
                recyclerView.addOnScrollListener(mListener);
            }
        });
    }
}
