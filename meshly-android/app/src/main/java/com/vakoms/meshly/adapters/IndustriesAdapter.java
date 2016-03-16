package com.vakoms.meshly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import meshly.vakoms.com.meshly.R;

/**
 * Created by Oleh Makhobey on 11.05.2015.
 * tajcig@ya.ru
 */
public class IndustriesAdapter extends ArrayAdapter<String>{

    private List<String> mChosenIndustries;

    public IndustriesAdapter (Context context ,List<String> chosenIndustries , List<String> _industriesList){

        super(context, R.layout.item_industries, _industriesList);
        this.mChosenIndustries = chosenIndustries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_industries, null, false);
            holder = new Holder();

            holder.tvIndustry = (TextView)   convertView.findViewById(R.id.tvIndustries_ItemIndustries);
            holder.ivIndustry = (ImageView)  convertView.findViewById(R.id.ivCheckbox_ItemIndustries);
            convertView.setTag(holder);
        }else {
            holder  =  (Holder)convertView.getTag();
        }
        setData(holder, getItem(position));
        return convertView;
    }

    private void setData(Holder holder,String _industry) {
       holder.tvIndustry.setText(_industry);

       switchIcons(holder.ivIndustry,  mChosenIndustries.contains(_industry));
    }

    public void switchIcons(ImageView _imageView,boolean _isPressed){
        if(_isPressed){
            _imageView.setAlpha(.8f);
            _imageView.setImageResource(R.drawable.check_box_checked);

        }else{
            _imageView.setAlpha(.5f);
            _imageView.setImageResource(R.drawable.check_box_unchecked);
        }
    }

    private class Holder {
        public TextView tvIndustry;
        public ImageView ivIndustry;
    }
}
