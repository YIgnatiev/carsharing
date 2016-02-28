package pro.theboard.fragments.cards_fragments;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionRadioBinding;

import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.cards.Answer;

/**
 * Created by Oleh Makhobey
 *    email : tajcig@ya.ru
 *    on 6/30/15.
 */
public class CardWithRadioButtonsFragment extends BaseFragment<FragmentQuestionRadioBinding> implements RadioGroup.OnCheckedChangeListener {

    private Answer mAnswer;
    private boolean isAnswered;


    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {
        b = DataBindingUtil.inflate(inflater,R.layout.fragment_question_radio,container,false);
        b.setListener(this);
        mAnswer = new Answer(mCardModel.getHash());
        addCheckboxItems();
        subscribeToNetwork();
        handleScroll(b.scrollView);

    }

    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmit.setEnabled(isNetworkActive);
        if(!isNetworkActive)Toast.makeText(mActivity,"No internet connection",Toast.LENGTH_SHORT).show();
    }


    private void addCheckboxItems(){
        float radius  = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource),radius);
        if(isPromo()){
            b.tvSubmit.setBackground(getResources().getDrawable(R.drawable.background_button_promo));
            b.tvSubmit.setText("Delete");
        }
        b.llBottom.setBackground(drawable);
        setData(b.tvQuestion,b.llQuestionLayout);

        LayoutInflater inflater = LayoutInflater.from(mActivity);

        for (int i = 0 ; i < mCardModel.getContent().size(); i++){

            RadioButton rbChoice  = (RadioButton)inflater.inflate(R.layout.item_radio, null);
            rbChoice.setText(mCardModel.getContent().get(i).getTitle());
            rbChoice.setId(rbChoice.getId()+i);
            rbChoice.setTag(mCardModel.getContent().get(i).getContent_hash());
            b.rgContainer.addView(rbChoice);

        }
        b.rgContainer.setOnCheckedChangeListener(this);

    }

    public void onAnswer(View view){
        if(isAnswered) {
           sendAnswer(mCardModel,mAnswer);
        }else {
            Toast.makeText(mActivity, getString(R.string.answer_question_first), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        isAnswered = true;
       RadioButton radioButton =  (RadioButton)group.findViewById(checkedId);
        String hash = (String)radioButton.getTag();
        mAnswer.addJustOneAnswer(hash);
    }
}
