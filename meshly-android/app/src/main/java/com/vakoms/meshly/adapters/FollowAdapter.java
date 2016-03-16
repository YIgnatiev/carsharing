package com.vakoms.meshly.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.interfaces.FollowItemListener;
import com.vakoms.meshly.interfaces.FollowListener;
import com.vakoms.meshly.models.NewUser;

import java.util.List;

import meshly.vakoms.com.meshly.databinding.PopularItem;

/**
 * Created by Oleh Makhobey on 20.10.2015.
 * tajcig@ya.ru
 */
public class FollowAdapter  extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {
    private List<NewUser> mList;
    private FollowItemListener mListener;


    public FollowAdapter(List<NewUser> list , FollowItemListener listener ) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PopularItem binding = PopularItem.inflate(inflater);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        NewUser user =mList.get(position);
        holder.b.setUser(user);
        holder.b.setListener(holder);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(FollowItemListener listener) {
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements FollowListener {

        PopularItem b;

        public MyViewHolder(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }


        @Override
        public void onFollow(View view) {
            NewUser user = b.getUser();
            mListener.onFollow(user);
        }
    }
}
