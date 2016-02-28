package com.carusselgroup.dwt.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carusselgroup.dwt.R;

public class OrderListAdapter extends BaseAdapter {
    private String[] mData;
    private Activity mContext;

    public OrderListAdapter(Activity context, String[] data) {
        this.mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.length - 1;
    }

    @Override
    public String getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mContext.getLayoutInflater().inflate(R.layout.car_list_spinner_item, parent, false);
        ((TextView) v.findViewById(R.id.Item_TextView_Spinner)).setText("");
        if (position == getCount()) {
            ((TextView) v.findViewById(R.id.Item_TextView_Spinner)).setText("");
            ((TextView) v.findViewById(R.id.Item_TextView_Spinner)).setHint(mData[position]);
        }
        else{
            ((TextView) v.findViewById(R.id.Item_TextView_Spinner)).setText(getItem(position));
        }
        return v;
    }
}
