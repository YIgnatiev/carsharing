package com.budivinictvo.quiz.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.budivinictvo.quiz.R;

/**
 * Created by Администратор on 04.01.2015.
 */


public class MenuAdapter extends ArrayAdapter {

    private boolean itemsEnabled = false;
    private Context mContext;
    private String[] mData;
    private LayoutInflater mInflater;
    private int textViewRessourceId;

    public MenuAdapter(Context context, int textViewResourceId, String[] data) {
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
        holder.setData(_position);

        return _convertView;
    }

    private class ViewHolder {

        public TextView textViewMenuText;
        public ImageView imageViewMenuIcon;


        public void initHolder(View _convertView) {
            this.textViewMenuText = (TextView) _convertView.findViewById(R.id.textView_drawer_layout_menu);
            this.imageViewMenuIcon = (ImageView) _convertView.findViewById(R.id.imageView_drawer_layout_icon);
        }

        public void setData(int position) {
            textViewMenuText.setText(mData[position]);
            TypedArray array = mContext.getResources().obtainTypedArray(R.array.menu_icons_for_drawer);
            int logoResourceId = array.getResourceId(position, 0);
            imageViewMenuIcon.setImageResource(logoResourceId);
        }
    }


}




