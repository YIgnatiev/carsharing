package pro.theboard.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentCardBinding;

import java.util.List;

import pro.theboard.BaseActivity;
import pro.theboard.adapters.CustomPageAdapter;
import pro.theboard.fragments.cards_fragments.BaseFragment;
import pro.theboard.listeners.OnAnswerListener;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.Model;
import pro.theboard.rest.RetrofitApi;
import rx.Subscription;

/**
 * Created by Oleh Makhobey on 14.04.2015.
 * tajcig@ya.ru
 */
public class CardsFragment extends Fragment implements OnAnswerListener {

    private BaseActivity mActivity;
    private CustomPageAdapter mCardsAdapter;
    private Subscription mDeleteSubscription;
    public boolean mIsPromo;
    private FragmentCardBinding b;
    private List<Model> mQuestions;

    public static CardsFragment getQuestionsInstance(List<Model> _list, boolean isPromo) {
        CardsFragment fragment = new CardsFragment();
        fragment.setIsPromo(isPromo);
        fragment.setQuestions(_list);
        return fragment;
    }

    public void setQuestions(List<Model> _questoins) {
        this.mQuestions = _questoins;
    }

    public void setIsPromo(boolean isPromo) {
        mIsPromo = isPromo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        b = DataBindingUtil.inflate(inflater, R.layout.fragment_card, container, false);
        mActivity = (BaseActivity) getActivity();
        fillFinishScreen();
        setAdapter();
        mActivity.setToolbarTitle(getString(R.string.toolbar_main));
        if (mQuestions != null && mQuestions.isEmpty()) finishCardGame();    /// null pointer
        return b.getRoot();
    }

    private void fillFinishScreen() {
        b.tvHeader.setText(R.string.finish_header);
        b.tvMessage.setText(Html.fromHtml(getString(R.string.finish_message)));
    }


    // adapter *****************

    private void setAdapter() {

        mCardsAdapter = new CustomPageAdapter(mQuestions, R.color.main_color, mIsPromo);
        if (mCardsAdapter.getCount() > 1) {
            addFragment(mCardsAdapter.getItem());
            addFragment(mCardsAdapter.getItem());
        } else if (mCardsAdapter.getCount() == 1) {
            addFragment(mCardsAdapter.getItem());
        }
    }

    private void addFragment(BaseFragment newFragment) {

        getFragmentManager()
                .beginTransaction()
                .add(R.id.clCards, newFragment)
                .commit();
    }

    @Override
    public void replaceFragment(Fragment fragment) {

        BaseFragment newFragment = mCardsAdapter.getItem();
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        fTransaction.remove(fragment);
        if(newFragment!= null && mCardsAdapter.getCount() != 1)fTransaction.add(R.id.clCards, newFragment);
        fTransaction.commit();


    }

    @Override
    public void onAnswer(Model _model, Answer answer) {
        removeQuestion(_model);
        if (mIsPromo) {
            deletePromoCard(_model.getHash());
            return;
        }

        checkSkip(_model);
        mActivity.sendAnswer(answer);

        if (mCardsAdapter.isEmpty()) finishCardGame();



    }

    // adapter  end ***************

    @Override
    public void onStop() {
        if (mDeleteSubscription != null) mDeleteSubscription.unsubscribe();
        super.onStop();
    }

    private void deletePromoCard(String hash) {

        mDeleteSubscription = RetrofitApi
                .getInstance(mActivity)
                .deletePromocard(hash)
                .subscribe((result) -> {
                }, (error) -> {
                });

    }

    private void checkSkip(Model model) {

    }

    private void finishCardGame() {
        b.llFinish.setVisibility(View.VISIBLE);
    }

    private void removeQuestion(Model _model) {
        mCardsAdapter.remove(_model);
    }
}
