package com.vakoms.meshly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vakoms.meshly.models.Event;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/31/15.
 */
public class ProfileEventsAdapter  {



        private TextView tvWebsite;
        private TextView tvName;
        private ImageView ivArrow;

    private Context mContext;
    private View.OnClickListener mListener;
    public  ProfileEventsAdapter(Context context,View.OnClickListener listener) {
        this.mContext = context;
        this.mListener = listener;

    }
    public View getInflatedView(Event event){

        View  convertView = LayoutInflater.from(mContext).inflate(R.layout.item_attending, null, false);

        ivArrow = (ImageView) convertView.findViewById(R.id.ivArrow_ItemAttending);
        tvName = (TextView) convertView.findViewById(R.id.tvName_itemAttending);
        tvWebsite = (TextView) convertView.findViewById(R.id.tvWebsite_itemAttending);
        setData(event);
        return convertView;

    }




    private void setData( Event event) {
       tvWebsite.setText(event.getUrl());
       tvName.setText(event.getName());
        ivArrow.setTag(event.getId());
        ivArrow.setOnClickListener(mListener);

    }




}




