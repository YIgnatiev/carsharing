package pro.theboard.fragments.cards_fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lapioworks.cards.R;

import pro.theboard.BaseActivity;
import pro.theboard.adapters.CustomPageAdapter;
import pro.theboard.constants.Constants;
import pro.theboard.custom_views.CustomScrollViev;
import pro.theboard.listeners.OnAnswerListener;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;
import pro.theboard.rest.RetrofitApi;
import pro.theboard.utils.NetworkUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.content.ContentObservable;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 22/03/15.
 */

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements
        View.OnTouchListener {
    protected Model mCardModel;
    protected int mColorResource;
    protected OnAnswerListener mOnAnswerListener;
    private Subscription mNetworkSubscription;
    protected BaseActivity mActivity;
    private boolean isPromo;
    private boolean isDeleted;

    private CustomPageAdapter mAdapter;
    protected T b; // databinding object

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        Fragment fragment = getFragmentManager().findFragmentByTag(Constants.CARDS_FRAGMENT);
        mOnAnswerListener = (OnAnswerListener) fragment;

        initCardView(inflater, container);
        b.getRoot().setTag(this);//save as listener
        return b.getRoot();
    }

    public void setAdapter(CustomPageAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onStop() {
        if (mNetworkSubscription != null) mNetworkSubscription.unsubscribe();
        super.onStop();
    }

    protected void sendAnswer(Model model, Answer answer) {
        mOnAnswerListener.onAnswer(model, answer);
        scaleOut();
    }

    //scrollview handling **********
    private boolean isScrollLocked;
    private boolean isFirstTime = true;
    private CustomScrollViev mScrollView;

    protected void handleScroll(CustomScrollViev scrollViev) {
        mScrollView = scrollViev;
        mScrollView.setOnTouchListener(this::handleScrollThouch);
        mScrollView.setCallback((bool) -> isScrollLocked = bool);
    }

    private boolean handleScrollThouch(View v, MotionEvent event) {

        X = event.getRawX();
        Y = event.getRawY();
        deltaX = X - startMoveX;
        deltaY = Y - startMoveY;
        Log.v("onScroll setted", "deltax = " + deltaX + " deltay = " + deltaY);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startMoveX = X;
            startMoveY = Y;
            isFirstTime = false;
        }

        int scrollViewHeihgt = mScrollView.getHeight();
        int contentHeight = mScrollView.getChildAt(0).getHeight();
        if (scrollViewHeihgt > contentHeight || isScrollLocked) {


            if (isFirstTime) {
                startMoveX = event.getRawX();
                startMoveY = event.getRawY();
                isFirstTime = false;
            }
            onTouch(b.getRoot(), event);

        } else {
            mScrollView.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isScrollLocked = false;
            isFirstTime = true;
        }
        return true;
    }

    // network handling ************

    protected abstract void handleNetwork(Boolean isNetworkActive);

    protected abstract void initCardView(LayoutInflater inflater, ViewGroup container);

    public void setCardModel(Model _model) {
        this.mCardModel = _model;
    }

    public void setColor(int _color) {
        mColorResource = _color;
    }

    protected void setData(TextView tvQuestion , LinearLayout llQuestionLayout) {

        String question = mCardModel.getQuestion();
        if (question.contains("http://") || question.contains("https://")|| question.contains("www.") ) {
            llQuestionLayout.removeAllViews();
            question = question.replaceAll("\n" ," ");
            StringBuilder builder = new StringBuilder();

            for(String word : question.split(" ")){

                if(word.contains("http://") || word.contains("https://") || word.contains("www.") ){

                    if(builder.length() != 0){
                        TextView textView =  (TextView) mActivity.getLayoutInflater().inflate(R.layout.item_question,null,false);
                        textView.setText(builder.toString());
                        llQuestionLayout.addView(textView);
                        builder = new StringBuilder();
                    }


                    TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.item_question,null,false);
                    textView.setLinksClickable(true);
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    textView.setAutoLinkMask(Linkify.WEB_URLS);
                    textView.setText(word);
                    llQuestionLayout.addView(textView);
                }else {
                    builder.append(word);
                    builder.append(" ");
                }
            }

            if(builder.length() != 0){
               TextView textView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.item_question,null,false);
                textView.setText(builder.toString());
                llQuestionLayout.addView(textView);
            }
        }else{
            tvQuestion.setText(question);
        }

        sendAsViewed(mCardModel.getHash());
    }




    public void setIsPromo(boolean isPromo) {
        this.isPromo = isPromo;
    }

    public boolean isPromo() {
        return isPromo;
    }

    //network broadcastReceiver
    protected void subscribeToNetwork() {
        mNetworkSubscription = listenToNetwork(mActivity)
                .subscribe(this::handleNetwork, throwable -> {
                });
    }

    private Observable<Boolean> listenToNetwork(Context context) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return ContentObservable
                .fromBroadcast(context, filter)
                .map(intent -> NetworkUtil.isNetworkAvailable(context))
                .distinct();

    }

    public void sendAsViewed(String cardHash) {
        RetrofitApi.getInstance(mActivity)
                .sendAsViewed(cardHash)
                .subscribe((response) -> {
                }, (throwable -> {
                }));
    }

    // touchListeners


    protected float startMoveX;
    protected float startMoveY;
    protected float X;
    protected float Y;
    protected float deltaX;
    protected float deltaY;
    private MotionEvent startEvent;

    public boolean onTouch(final View view, MotionEvent event) {

        if (startEvent == null) startEvent = event;
        X = event.getRawX();
        Y = event.getRawY();
        deltaX = X - startMoveX;
        deltaY = Y - startMoveY;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                startMoveX = X;
                startMoveY = Y;
                startEvent = event;
              //  animateOnDown(view);
                break;

            case MotionEvent.ACTION_UP:
               // animateOnUp(view);


                long timeSpended = event.getEventTime() - startEvent.getDownTime();

                double dist = Math.sqrt(Math.pow(startEvent.getX() - event.getX(), 2) + Math.pow(startEvent.getY() - event.getY(), 2));

                double velocity = dist / timeSpended;
                Log.v("myFling", "time spended = " + timeSpended + " distance = " + dist + " velocity  = " + dist / timeSpended);
                if (velocity > 5 ) {
                        moveOut(b.getRoot(),deltaX,deltaY);
                    return true;
                }


                if ((Math.abs(deltaY) > view.getHeight() / 2 || Math.abs(deltaX) > view.getWidth() / 2) && mAdapter.getCount() != 1) {
                    moveOut(view, deltaX, deltaY);
                } else
                    moveBack(view);
                break;

            case MotionEvent.ACTION_MOVE:
                view.setTranslationY(deltaY);
                view.setTranslationX(deltaX);
                ((View)view.getParent()).invalidate();

                break;
        }
        return true;
    }


    private void animateOnDown(View view) {
        view.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(100);
    }

    private void animateOnUp(View view) {
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(100);
    }


    private void moveBack(final View view) {

        view.animate()
                .translationX(0)
                .translationY(0)
                        //.setInterpolator(new AnticipateOvershootInterpolator())
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setDuration(300);
    }

    public void scaleOut() {


        b.getRoot().animate().alpha(0)
                .scaleX(2)
                .scaleY(2)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mOnAnswerListener.replaceFragment(BaseFragment.this);
                        b.getRoot().animate().setListener(null);
                    }
                });
    }

    private void moveOut(final View view, float xTranslation, float yTranslation) {

        view.animate()
                .translationXBy(xTranslation)
                .translationYBy(yTranslation)
                .setDuration(100)
                .alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        mOnAnswerListener.replaceFragment(BaseFragment.this);

                        view.animate().setListener(null);
                    }
                });
    }


}
