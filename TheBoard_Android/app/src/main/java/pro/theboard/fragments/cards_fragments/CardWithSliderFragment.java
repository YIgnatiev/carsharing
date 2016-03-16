package pro.theboard.fragments.cards_fragments;

/**
 * Created by lapioworks on 6/30/15.
 */

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionSliderBinding;

import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.RangeModel;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 22/03/15.
 */
public class CardWithSliderFragment extends BaseFragment<FragmentQuestionSliderBinding> implements SeekBar.OnSeekBarChangeListener {

    private int mRangePosition = 5;

    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_question_slider, container, false);
        b.setListener(this);
        b.sbRange.setOnSeekBarChangeListener(this);
        setQuestion();
        subscribeToNetwork();

    }


    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmit.setEnabled(isNetworkActive);
        if (!isNetworkActive)
            Toast.makeText(mActivity, "No internet connection", Toast.LENGTH_SHORT).show();
    }



    private void setQuestion() {
        float radius = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource), radius);

        b.llBottom.setBackground(drawable);
        b.tvOption2.setVisibility(View.GONE);

        String range = mCardModel.getContent().get(0).getTitle();
        RangeModel rangeModel = new Gson().fromJson(range, RangeModel.class);
        b.tvOption1.setText(rangeModel.getLowestText());
        b.tvOption3.setText(rangeModel.getHighestText());
        setData(b.tvQuestion,b.llQuestionLayout);

    }


    public void onAnswer(View view) {
        Answer answer = new Answer(mCardModel.getHash());
        answer.addData(String.valueOf(mRangePosition));
        sendAnswer(mCardModel, answer);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mRangePosition = progress / 10;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
