package pro.theboard.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentAlertCardBinding;

import java.util.ArrayList;
import java.util.List;

import pro.theboard.LoginActivity;
import pro.theboard.adapters.CustomPageAdapter;
import pro.theboard.constants.Constants;
import pro.theboard.fragments.cards_fragments.BaseFragment;
import pro.theboard.listeners.OnAnswerListener;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.ContentModel;
import pro.theboard.models.cards.Model;
import pro.theboard.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/20/15.
 */
public class K18CardFragment extends Fragment implements OnAnswerListener {


    private final String YES = "isAdult";
    private LoginActivity mActivity;
    private CustomPageAdapter mCardsAdapter;
    private List<Model> mQuestoins;

   private FragmentAlertCardBinding b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater,R.layout.fragment_alert_card, container, false);
        mActivity = (LoginActivity) getActivity();
        fillFinishScreen();
        setAdapter();
        mActivity.setToolbarTitle("K18");
        return b.getRoot();
    }

    private void fillFinishScreen() {
        b.tvMessage.setText(Html.fromHtml(getString(R.string.finish_alert_message)));
    }

    private void setAdapter() {
        initAlertCard();
        mCardsAdapter = new CustomPageAdapter(mQuestoins, R.color.footer_18, false);
        addFragment(mCardsAdapter.getItem());
    }

    private void addFragment(BaseFragment newFragment) {

        getFragmentManager()
                .beginTransaction()
                .add(R.id.clCards, newFragment)
                .commit();
    }

    private void initAlertCard() {
        mQuestoins = new ArrayList<>();
        Model model = new Model();

        model.setType(Constants.TYPE_SINGLE);
        model.setQuestion("Oletko täysi-ikäinen?");


        List<ContentModel> list = new ArrayList<>();
        ContentModel item = new ContentModel("", "");
        ContentModel item2 = new ContentModel("", "{'yesLabel' : 'KYLLÄ'  ,   'noLabel' : 'EI'}");
        item.setContent_hash(YES);
        list.add(item);
        list.add(item2);
        model.setContent(list);
        mQuestoins.add(model);
    }


    @Override
    public void replaceFragment(Fragment fragment) {

    }

    @Override
    public void onAnswer(Model _model, Answer answer) {
        removeQuestion(_model);


        if (answer.isYes()) {
            Preferences.saveIsAdult(true);
            mActivity.requestLogin();
        } else {
            finishCardGame();
        }
    }


    private void finishCardGame() {
        b.llFinish.setVisibility(View.VISIBLE);
        startHandler();
    }

    private void startHandler() {
        new Handler().postDelayed(mActivity::finish, 3000);
    }

    private void removeQuestion(Model _model) {
        mCardsAdapter.remove(_model);
    }
}


