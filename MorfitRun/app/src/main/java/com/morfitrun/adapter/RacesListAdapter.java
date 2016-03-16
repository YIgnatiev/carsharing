package com.morfitrun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.morfitrun.R;
import com.morfitrun.data_models.race_model.Race;
import java.util.ArrayList;

/**
 * Created by vasia on 11.03.2015.
 */
public class RacesListAdapter extends ArrayAdapter<Race> {

    private ArrayList<Race> mRacesData;
    private Context mContext;
    private LayoutInflater mInflater;

    public RacesListAdapter(Context _context, ArrayList<Race> _racesData){
        super(_context,R.layout.item_races,_racesData);
        mContext = _context;
        mRacesData = _racesData;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mRacesData.size();
    }

    @Override
    public Race getItem(int position) {
        return mRacesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;
        if (view == null){
            view = mInflater.inflate(R.layout.item_races, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvRacesTitle = (TextView) view.findViewById(R.id.tvRacesTitle);
            viewHolder.tvRacesInfo = (TextView) view.findViewById(R.id.tvRacesInfo);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();
        final Race race = mRacesData.get(position);
        viewHolder.tvRacesTitle.setText(race.getTitle());
        viewHolder.tvRacesInfo.setText(race.getDescription());
        return view;

    }

    private class ViewHolder{
        private TextView tvRacesTitle;
        private TextView tvRacesInfo;
    }
}
