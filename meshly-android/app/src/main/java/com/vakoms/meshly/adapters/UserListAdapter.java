package com.vakoms.meshly.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.interfaces.UserItemListListener;
import com.vakoms.meshly.interfaces.UserListListener;
import com.vakoms.meshly.models.NewUser;

import java.util.List;

import meshly.vakoms.com.meshly.databinding.ItemUserBinding;

/**
 * Created by taras.melko on 15.09.2014.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private List<NewUser> mList;
    private UserItemListListener mListener;


    public UserListAdapter(List<NewUser> list) {
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemUserBinding binding = ItemUserBinding.inflate(inflater, parent, false);
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

    public void setListener(UserItemListListener listener) {
            this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements UserListListener{
       ItemUserBinding b;

        public MyViewHolder(View itemView) {
            super(itemView);
            b = DataBindingUtil.bind(itemView);
        }


        @Override
        public void onAvatarClicked(View view ) {
             NewUser user = b.getUser();

            mListener.onAvatarClicked(user.getId());
        }

        @Override
        public void onItemClicked(View view) {
            NewUser user = b.getUser();

            mListener.onItemClicked(user.getId());
        }
    }
}