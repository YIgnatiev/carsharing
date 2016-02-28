package com.carusselgroup.dwt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.model.Car;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarsListAdapter extends ArrayAdapter {
    private Context mContext;
    private List<Car> mData;
    private LayoutInflater mInflater;

    public CarsListAdapter(Context context, int textViewResourceId, List<Car> data) {
        super(context, textViewResourceId, data);
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(R.layout.cars_list_item, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        holder.setData(mData.get(_position));
        if (_position % 2 ==0){
            holder.linerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_background_dark));
        }else {
            holder.linerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_item_background_light));
        }




        return _convertView;
    }

    private class ViewHolder {

        public TextView textViewCharacteristics;
        public TextView textViewCarName;
        public TextView textViewPrice;
        public ImageView imageViewMedium;
        public LinearLayout linerLayout;

        public void initHolder(View _convertView) {
            this.textViewCharacteristics = (TextView) _convertView.findViewById(R.id.textView_characteristics_CarListFragment);
            this.linerLayout = (LinearLayout)_convertView.findViewById(R.id.liearLayout_list_item);
            this.textViewPrice = (TextView) _convertView.findViewById(R.id.textView_price_CarListFragment);
            this.textViewCarName = (TextView) _convertView.findViewById(R.id.textView_car_name_CarListFragment);
            this.imageViewMedium = (ImageView) _convertView.findViewById(R.id.imageView_car_image_medium_CarListFragment);
        }

        public void setData(Car _car) {
            textViewCarName.setText(_car.getBrand() + " " + _car.getModel());

            textViewPrice.setText(_car.getPrice() + " FT");
            if (_car.getDefaultImage() != null) {
                String title = _car.getCharacteristics();
                String[] characteristics = title.split(",");
                String characteristic = characteristics.length > 3 ? characteristics[1] + "\n" + characteristics[2] : characteristics[characteristics.length - 1];
                textViewCharacteristics.setText(characteristic);
                Picasso.with(mContext).load(_car.getDefaultImage().getMediumUrl()).into(imageViewMedium);
            }
        }
    }
}
