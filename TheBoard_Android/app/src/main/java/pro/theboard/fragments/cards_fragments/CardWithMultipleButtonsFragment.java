package pro.theboard.fragments.cards_fragments;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionWithMulitpleVariantsBinding;

import java.util.List;

import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.SingleButtonModel;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.ContentModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 22/03/15.
 */

public class CardWithMultipleButtonsFragment extends BaseFragment<FragmentQuestionWithMulitpleVariantsBinding> {

    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_question_with_mulitple_variants, container, false);
        b.setListener(this);
        setQuestion();
        subscribeToNetwork();
    }

    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmitNo.setEnabled(isNetworkActive);
        b.tvSubmitYes.setEnabled(isNetworkActive);
        if (!isNetworkActive)
            Toast.makeText(mActivity, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    private void setQuestion() {
        float radius = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource), radius);

        b.llBottom.setBackground(drawable);
        setData(b.tvQuestion,b.llQuestionLayout);

        List<ContentModel> contentModelList = mCardModel.getContent();
        ContentModel modelWithButtonInfo = contentModelList.get(1);
        SingleButtonModel model = new Gson().fromJson(modelWithButtonInfo.getTitle(), SingleButtonModel.class);
        b.tvSubmitNo.setText(model.getNoLabel());
        b.tvSubmitYes.setText(model.getYesLabel());
    }

    public void onAnswerNo(View view){
        Answer answer = new Answer(mCardModel.getHash());
        answer.addData("0");
        sendAnswer(mCardModel, answer);
    }

    public void onAnswerYes(View view){
        Answer answer = new Answer(mCardModel.getHash());
        answer.addData("1");
        sendAnswer(mCardModel, answer);
    }

}



