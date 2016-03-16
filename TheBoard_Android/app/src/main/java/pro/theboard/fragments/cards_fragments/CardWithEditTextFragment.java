package pro.theboard.fragments.cards_fragments;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionEdittextBinding;

import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.cards.Answer;
import pro.theboard.utils.KeyboardUtil;
/**
 * Created by Oleh Makhobey
 *    email : tajcig@ya.ru
 *    on 22/03/15.
 */

public class CardWithEditTextFragment extends BaseFragment<FragmentQuestionEdittextBinding> {

    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {
        b = DataBindingUtil.inflate(inflater,R.layout.fragment_question_edittext,container,false);
        b.setListener(this);
        setQuestion();
        subscribeToNetwork();
    }

    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmit.setEnabled(isNetworkActive);
        if(!isNetworkActive) Toast.makeText(mActivity, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        KeyboardUtil.hideKeyBoard(b.etAnswer, mActivity);
    }

    private void setQuestion() {
        float radius  = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource),radius);

        if(isPromo()){
            b.tvSubmit.setBackground(getResources().getDrawable(R.drawable.background_button_promo));
            b.tvSubmit.setText("Delete");
        }
        b.llBottom.setBackground(drawable);
        setData(b.tvQuestion , b.llQuestionLayout);
    }

    private boolean checkFields(){
        String text  = b.etAnswer.getText().toString();
        if(text.isEmpty()) {
            b.etAnswer.setError(getString(R.string.you_have_to_answer_first));
            return false;
        }else return true;
    }

    public void onAnswer(View view ){
        if(checkFields()){
            Answer answer = new Answer(mCardModel.getHash());
            answer.addData(b.etAnswer.getText().toString());
            sendAnswer(mCardModel, answer);
        }

    }
}
