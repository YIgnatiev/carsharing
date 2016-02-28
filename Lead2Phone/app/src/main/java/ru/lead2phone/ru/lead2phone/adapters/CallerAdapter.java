package ru.lead2phone.ru.lead2phone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.lead2phone.ru.lead2phone.R;
import ru.lead2phone.ru.lead2phone.models.Caller;

/**
 * Created by Oleh Makhobey on 27.08.2015.
 * tajcig@ya.ru
 */
public class CallerAdapter extends ArrayAdapter<Caller> {


    private View.OnClickListener mListener;

    public CallerAdapter(Context context, List<Caller> list,View.OnClickListener listener) {
        super(context, R.layout.item_caller, list);
        mListener = listener;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_caller, null, false);
            holder = new Holder();

            holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber_ItemCaller);
            holder.ivCall = (ImageView) convertView.findViewById(R.id.ivCall_ItemCaller);
            holder.ivCancelCall = (ImageView) convertView.findViewById(R.id.ivDelete_ItemCaller);



            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        setData(holder, position);
        return convertView;
    }





    private void setData(final Holder holder,  int position) {


       holder.tvNumber.setText(getItem(position).getNumber());
        holder.ivCancelCall.setOnClickListener(mListener);
        holder.ivCancelCall.setTag(position);

        holder.ivCall.setOnClickListener(mListener);
        holder.ivCall.setTag(position);

    }

    private class Holder {
        public TextView tvNumber;
        public ImageView ivCall;
        public ImageView ivCancelCall;


    }


}
