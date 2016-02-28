package com.vakoms.meshly.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.BaseHolder;
import com.vakoms.meshly.interfaces.PeopleItemListener;
import com.vakoms.meshly.interfaces.PeopleListener;
import com.vakoms.meshly.models.NewUser;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.PeopleItem;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/14/15.
 */


public class PeopleAdapter extends RecyclerView.Adapter<BaseHolder> {


    private PeopleItemListener mListener;
    private List<NewUser> mList;

    public PeopleAdapter(PeopleItemListener listener) {
        this.mList = new ArrayList<>();
        this.mListener = listener;
    }

    public List<NewUser> getList() {
        return mList;
    }


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case BaseHolder.TYPE_ITEM:
                View view = PeopleItem.inflate(inflater, parent, false).getRoot();
                return new ViewHolder(view ,BaseHolder.TYPE_ITEM);

            case BaseHolder.TYPE_HEADER:
                View header = inflater.inflate(R.layout.item_empty_header, null);
                return new HeaderHolder(header,BaseHolder.TYPE_HEADER);
            default:
                throw new RuntimeException("You have to define your item type");


        }
    }


    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        switch (holder.getItemType()) {
            case BaseHolder.TYPE_HEADER:

                break;
            case BaseHolder.TYPE_ITEM:

                ViewHolder viewHolder = (ViewHolder) holder;
                NewUser user = mList.get(position -1);
                // avoiding empty users

                if(user.getSummary() == null || user.getSummary().isEmpty())user.setSummary("Open for\nbusiness connections");
              //  if(user.getJob()== null || user.getJob().isEmpty())user.setJob("Unknown");

                viewHolder.b.setUser(user);
                viewHolder.b.setListener(viewHolder);
                break;
        }


    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return BaseHolder.TYPE_HEADER;

        return BaseHolder.TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return (mList.size() + 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public class ViewHolder extends BaseHolder implements PeopleListener {
        PeopleItem b;

        public ViewHolder(View view ,int type) {
            super(view,type);
            b = DataBindingUtil.bind(view);


        }

        @Override
        public void onFollow(View view) {
            mListener.onFollow(b.getUser());
        }

        @Override
        public void onChat(View view) {
            mListener.onChat(b.getUser().getId());
        }

        @Override
        public void onEvent(View view) {
            //TODO make event
        }

        @Override
        public void onProfile(View view) {
            mListener.onProfile(b.getRoot(), b.getUser());
        }
    }


    public class HeaderHolder extends BaseHolder {

        public HeaderHolder(View itemView ,int type) {
            super(itemView,type);
        }
    }


}

