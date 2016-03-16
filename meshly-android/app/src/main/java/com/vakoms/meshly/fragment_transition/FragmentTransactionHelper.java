package com.vakoms.meshly.fragment_transition;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.vakoms.meshly.utils.DensityConvertor;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/9/15.
 */
public class FragmentTransactionHelper implements View.OnKeyListener ,ViewTreeObserver.OnPreDrawListener  {
    private ViewGroup mainContainer;

    private ViewGroup oldView;
    private ViewGroup newView;
    private Fragment fragment;
    private Transition mTransition;



    private Scene enterScene;
    private Scene exitScene;



    public FragmentTransactionHelper(ViewGroup oldView, ViewGroup newView,ViewGroup mainContainer, Transition transition,Fragment fragment) {
        this.oldView = oldView;
        this.newView = newView;
        this.fragment = fragment;
        this.mainContainer = mainContainer;
        this.mTransition = transition;

      init();
    }



    private void init(){
        setOnBackListener(newView);

        mainContainer.removeView(oldView);

        oldView.getViewTreeObserver().addOnPreDrawListener(this);


        enterScene = new Scene(mainContainer,newView);
        exitScene = new Scene(mainContainer,oldView);

    }








    public static ViewGroup getCloneItem(RecyclerView listView , View itemView ,View bindedView){


        int t = itemView.getTop() + listView.getTop() + (int)DensityConvertor.pxFromDp(itemView.getContext(),50);
        int l = itemView.getLeft() + listView.getLeft();


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemView.getWidth(), itemView.getHeight());
        lp.topMargin = t;
        lp.leftMargin = l;

//        final TransitionSet set = new TransitionSet();
//        set.addTransition(new Fade());
//        set.setDuration(500);
//        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
//        set.setPropagation(new CircularPropagation());
//        set.setInterpolator(new FastOutSlowInInterpolator());


        final RelativeLayout rl = new RelativeLayout(bindedView.getContext());
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        rl.setLayoutParams(rlp);
        bindedView.setLayoutParams(lp);
        rl.addView(bindedView);

      return rl;



    }


    private void setOnBackListener(View view) {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

    }







    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() != KeyEvent.ACTION_UP) {
                return true;
            }

            //mListener.onBackClicked();
           onBackPressed();
            return true;

        }
        return false;
    }


    public void onBackPressed(){


        mTransition.addListener(new Transition.TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                if(fragment != null)
                fragment.getFragmentManager().popBackStack();
            }
        });


        TransitionManager.go(exitScene, mTransition);
    }







    boolean justOnce = true;
    @Override
    public boolean onPreDraw() {
        if (justOnce) {
            justOnce = false;

            TransitionManager.go(enterScene, mTransition);

        }
        return true;
    }






    public interface OnBackListener {
        void onBackClicked();
    }

}
