package com.budivnictvo.rssnews.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.data.RssCategory;
import com.budivnictvo.rssnews.app.data.RssItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;



/**
 * Created by Администратор on 16.12.2014.
 */
public class CategoryAdapter extends ArrayAdapter {
    private Context mContext;
    private RssCategory[] mData;
    private LayoutInflater mInflater;
    private int mLayoutId;

    public CategoryAdapter (Context _context, int _textViewResourceId, RssCategory[] _data) {
        super(_context, _textViewResourceId, _data);
        this.mContext = _context;
        this.mData = _data;
        this.mLayoutId = _textViewResourceId;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(mLayoutId, _parent, false);
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

        public TextView tvCategoryText;
        public ImageView ivCategoryIcon;

        public void initHolder(View _convertView) {
            this.tvCategoryText = (TextView) _convertView.findViewById(R.id.tvCategory);
            this.ivCategoryIcon = (ImageView)_convertView.findViewById(R.id.ivCategory);
        }

        public void setData(RssCategory  _item) {
            tvCategoryText.setText(_item.getName());
            ivCategoryIcon.setImageDrawable(mContext.getResources().getDrawable(_item.getIconId()));

        }
    }
}

