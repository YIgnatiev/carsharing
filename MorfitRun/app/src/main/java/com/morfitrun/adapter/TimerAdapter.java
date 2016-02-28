package com.morfitrun.adapter;

/**
 * Created by Admin on 17.03.2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.morfitrun.R;
import com.morfitrun.data_models.Runner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 16.03.2015.
 */
public class TimerAdapter extends ArrayAdapter<Runner> {
    private LayoutInflater mInflater;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.UK);

    public TimerAdapter(Context _context, int _resId ,List<Runner> _list){
        super(_context,_resId,_list);
    }



    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(R.layout.item_timer, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        if(_position%2 ==0){
            _convertView.setBackgroundColor(getContext().getResources().getColor(R.color.item_background_grey));
        }else{
            _convertView.setBackgroundColor(getContext().getResources().getColor(R.color.item_background_white));
        }


        holder.setData(getItem(_position));

        return _convertView;
    }

    private class ViewHolder {

        public TextView tvPosition;
        public TextView tvTime;
        public TextView tvName;


        public void initHolder(View _convertView) {
            this.tvPosition  = (TextView) _convertView.findViewById(R.id.tvPosition_itemTimer);
            this.tvTime = (TextView) _convertView.findViewById(R.id.tvTime_itemTimer);
            this.tvName = (TextView)_convertView.findViewById(R.id.tvName_itemTimer);


        }

        public void setData(Runner _runner) {
           tvPosition.setText(String.valueOf(_runner.get_position()));
           Date date = new Date(_runner.getTime());
           tvTime.setText(mDateFormat.format(date));
           if(_runner.getUserId() == null){
                tvName.setText(getContext().getString(R.string.tbd));
           }else {
               tvName.setText(_runner.getUserId().getFullName());
           }
        }
    }

}
