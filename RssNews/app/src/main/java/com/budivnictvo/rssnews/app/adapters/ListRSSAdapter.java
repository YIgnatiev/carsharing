package com.budivnictvo.rssnews.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.data.RssItem;
import com.budivnictvo.rssnews.app.utils.DateFormater;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by Администратор on 16.12.2014.
 */
public class ListRSSAdapter extends ArrayAdapter {
    private Context mContext;
    private List<RssItem> mData;
    private int mResource;
    private  LayoutInflater mInflater;

    public ListRSSAdapter(Context context, int resource, int textViewResourceId, List data) {
        super(context, resource, textViewResourceId, data);
        this.mContext = context;
        this.mData = data;
        this.mResource = resource;

    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null){
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         }


        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(R.layout.list_item, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        holder.setData(mData.get(_position));

        return _convertView;
    }
    private class ViewHolder {

        public TextView tvTitle;
        public TextView tvText;
        public TextView tvDate;
        public ImageView ivItemImage;

        public void initHolder(View _convertView) {
            this.tvTitle = (TextView) _convertView.findViewById(R.id.tvItemTitle);
            this.tvText = (TextView) _convertView.findViewById(R.id.tvItemText);
            this.tvDate = (TextView) _convertView.findViewById(R.id.tvItemDate);
            this.ivItemImage = (ImageView)_convertView.findViewById(R.id.ivItemImage);
        }

        public void setData(RssItem _item) {
            tvTitle.setText(_item.getTitle());
            tvText.setText(_item.getDescription());
            Date date = DateFormater.dateParser(_item.getPubDate());
            tvDate.setText(DateFormater.dateFormat(date));
            Picasso.with(mContext).load(_item.getImageUrl()).into(ivItemImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
            }
        }
}
