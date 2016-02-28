package com.budivinictvo.quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.budivinictvo.quiz.R;
import com.budivinictvo.quiz.model.AnswerVariants;


/**
 * Created by Администратор on 02.01.2015.
 */
public class AnswersAdapter extends ArrayAdapter {

    private boolean itemsEnabled = false;
    private Context mContext;
    private AnswerVariants[] mData;
    private LayoutInflater mInflater;
    private int textViewRessourceId;

    public AnswersAdapter(Context context, int textViewResourceId, AnswerVariants[] data) {
        super(context, textViewResourceId, data);
        this.mContext = context;
        this.mData = data;
        this.textViewRessourceId = textViewResourceId;


    }

    public void setItemsEnabled(boolean enabled) {
        itemsEnabled = enabled;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(textViewRessourceId, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        holder.setData(mData[_position]);

        return _convertView;
    }

    private class ViewHolder {

        public TextView textAnswer;


        public void initHolder(View _convertView) {
            this.textAnswer = (TextView) _convertView.findViewById(R.id.textView_answer_item);
        }

        public void setData(AnswerVariants _answerVariants) {
            textAnswer.setText(_answerVariants.getTitle());
            textAnswer.setEnabled(itemsEnabled);
        }
    }

    @Override
    public boolean isEnabled(int position) {

        return itemsEnabled;
    }
}


