package pro.theboard.fragments.cards_fragments;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionWithCheckboxesBinding;

import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.cards.Answer;

/**
 * Created by Oleh Makhobey
 *    email : tajcig@ya.ru
 *    on 22/03/15.
 */

public class CardWithCheckboxesFragment extends BaseFragment<FragmentQuestionWithCheckboxesBinding> {

    private CheckBox [] arrayOfCheckBoxes;
    private boolean isAnswered;

    @Override
    protected void initCardView(LayoutInflater inflater , ViewGroup container) {
        b = DataBindingUtil.inflate(inflater,R.layout.fragment_question_with_checkboxes,container,false);
        b.setListener(this);
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
        arrayOfCheckBoxes = new CheckBox[mCardModel.getContent().size()];
        for (int i = 0 ; i < mCardModel.getContent().size(); i++){
            View view =  inflater.inflate(R.layout.item_checkbox , null);
            arrayOfCheckBoxes[i]  = (CheckBox)view.findViewById(R.id.cb_item_choice);
            arrayOfCheckBoxes[i].setText(mCardModel.getContent().get(i).getTitle());
            arrayOfCheckBoxes[i].setTag(mCardModel.getContent().get(i).getContent_hash());
            b.llCheckboxContainer.addView(view);
        }

    }

    public void onAnswer(View view){
        Answer answer = new Answer(mCardModel.getHash());
        isAnswered = false;
        for (CheckBox checkBox :arrayOfCheckBoxes) {
            if (checkBox.isChecked()) {
                isAnswered  = true;
                answer.addAnswer((String) checkBox.getTag());
            }
        }

        if(isAnswered)
            sendAnswer(mCardModel,answer);
        else Toast.makeText(mActivity,getString(R.string.answer_question_first),Toast.LENGTH_SHORT).show();
    }




}
