package com.vakoms.meshly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vakoms.meshly.MainActivity;
import com.vakoms.meshly.models.Message;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.TimeCounterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import meshly.vakoms.com.meshly.R;

/**
 * Created by taras.teslyuk on 15.09.2014.
 */
public class ChatAdapter extends BaseAdapter {

    private List<Message> mList = new ArrayList<Message>();
    private LayoutInflater mInflater;
    private Context mContext;
    private static final long TIME_UPDATE = 20000;
    private LinearLayout.LayoutParams lp;


    public ChatAdapter(Context context, List<Message> list) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 8, 0, 0);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
//        if (convertView == null) {
        holder = new ViewHolder();

        convertView = mInflater.inflate(R.layout.item_message, parent, false);

        holder.meText = (TextView) convertView.findViewById(R.id.item_message_me_text);
        holder.meTime = (TextView) convertView.findViewById(R.id.item_message_me_time_tv);
        holder.meLayout = (LinearLayout) convertView.findViewById(R.id.item_message_me_ll);
        holder.meTriangle = (LinearLayout) convertView.findViewById(R.id.item_message_me_triangle_ll);

        holder.toText = (TextView) convertView.findViewById(R.id.item_message_to_text);
        holder.toTime = (TextView) convertView.findViewById(R.id.item_message_to_time_tv);
        holder.toLayout = (LinearLayout) convertView.findViewById(R.id.item_message_to_ll);
        holder.toTriangle = (LinearLayout) convertView.findViewById(R.id.item_message_to_triangle_ll);

        convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        final Message item = mList.get(position);


        if (!item.getUser_id().equals(((MainActivity)mContext).mUser.getId())) {
            holder.toLayout.setVisibility(View.GONE);
            holder.meText.setText(item.getMessage() != null ? item.getMessage() : "");

            if (position != 0 && mList.get(position - 1) != null && mList.get(position - 1).getUser_id().equals(mList.get(position).getUser_id())) {
                holder.meTriangle.setVisibility(View.GONE);
                holder.meLayout.setLayoutParams(lp);
            } else {
                startUpdateMe(holder, Long.parseLong(mList.get(position).getTimestamp().replace(".", "").substring(0, 10)));
            }
        } else {
            holder.meLayout.setVisibility(View.GONE);
            holder.toText.setText(item.getMessage() != null ? item.getMessage() : "");

            if (position != 0 && mList.get(position - 1) != null && mList.get(position - 1).getUser_id().equals(((MainActivity)mContext).mUser.getId())) {
                holder.toTriangle.setVisibility(View.GONE);
                holder.toLayout.setLayoutParams(lp);
            } else {
                startUpdateTo(holder, Long.parseLong(mList.get(position).getTimestamp().replace(".", "").substring(0, 10)));
            }

        }

        return convertView;
    }


    public void startUpdateMe(final ViewHolder holder, final long time) {
        updateMe(holder, time);
        Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateMe(holder, time);
            }
        };
        timer.schedule(task, 0, TIME_UPDATE);
    }

    private void updateMe(final ViewHolder holder, final long lastTime) {
        ((MainActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String time = TimeCounterUtil.calculateTimeInChat(lastTime);
                holder.meTime.setText(time != null ? time : "");
            }
        });
    }

    public void startUpdateTo(final ViewHolder holder, final long time) {
        updateTo(holder, time);
        Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateTo(holder, time);
            }
        };
        timer.schedule(task, 0, TIME_UPDATE);
    }

    private void updateTo(final ViewHolder holder, final long lastTime) {
        ((MainActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String time = TimeCounterUtil.calculateTimeInChat(lastTime);
                holder.toTime.setText(time != null ? time : "");
            }
        });
    }

    class ViewHolder {

        TextView toTime;
        LinearLayout toLayout;
        TextView toText;
        LinearLayout toTriangle;

        TextView meTime;
        LinearLayout meLayout;
        TextView meText;
        LinearLayout meTriangle;
    }

}