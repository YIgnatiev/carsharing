package com.morfitrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morfitrun.R;
import com.morfitrun.data_models.RunnerUser;
import com.morfitrun.data_models.User;

import java.util.List;

/**
 * Created by Admin on 16.03.2015.
 */
public class RegisteredRunnersListAdapter extends ArrayAdapter <RunnerUser> {
    private LayoutInflater mInflater;

    public RegisteredRunnersListAdapter(Context _context, int _resId ,List<RunnerUser> _list){
        super(_context,_resId,_list);
    }



    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        ViewHolder holder = null;
        if (_convertView == null) {
            _convertView = mInflater.inflate(R.layout.item_registered_runners, _parent, false);
            holder = new ViewHolder();
            holder.initHolder(_convertView);
            _convertView.setTag(holder);
        } else {
            holder = (ViewHolder) _convertView.getTag();
        }
        holder.setData(getItem(_position));

        return _convertView;
        }

    private class ViewHolder {

        public TextView tvName;
        public TextView tvEmail;
        public ImageView ivAvatar;


        public void initHolder(View _convertView) {
            this.tvName  = (TextView) _convertView.findViewById(R.id.tvName_item_FRegRun);
            this.tvEmail = (TextView) _convertView.findViewById(R.id.tvEmail_item_FRegRun);
            this.ivAvatar = (ImageView)_convertView.findViewById(R.id.ivAvatar_item_FRegRun);
        }

        public void setData(RunnerUser _user) {
           tvName.setText(_user.getFullName());
           tvEmail.setText(_user.getBirthDate());
        }
    }

}
