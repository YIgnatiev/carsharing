package com.vakoms.meshly.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.interfaces.IndustriesItemListener;
import com.vakoms.meshly.interfaces.IndustriesListener;
import com.vakoms.meshly.models.Industry;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.databinding.ItemNewIndustriesBinding;

/**
 * Created by Oleh Makhobey on 20.10.2015.
 * tajcig@ya.ru
 */
public class IndustriesRecycleAdapter extends RecyclerView.Adapter<IndustriesRecycleAdapter.MyViewHolder> {
        private List<Industry> mList;
        private List<String> mUserList;


        public IndustriesRecycleAdapter(List<Industry> list, List<String> userList ) {
            this.mList = list;
            this.mUserList = userList  != null ? userList: new ArrayList<>();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemNewIndustriesBinding binding = ItemNewIndustriesBinding.inflate(inflater);
            return new MyViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Industry industry =mList.get(position);
            holder.b.setData(industry);
            holder.b.setIsChoosen(mUserList.contains(industry.getName()));
            holder.b.setListener(holder);

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }



        class MyViewHolder extends RecyclerView.ViewHolder implements IndustriesListener {

            ItemNewIndustriesBinding b;

            public MyViewHolder(View itemView) {
                super(itemView);
                b = DataBindingUtil.bind(itemView);
            }


            @Override
            public void onIndustries(View view) {
                Industry industry= b.getData();
                if(mUserList.contains(industry.getName())){
                    mUserList.remove(industry.getName());
                    b.setIsChoosen(false);
                }else {
                    mUserList.add(industry.getName());
                    b.setIsChoosen(true);
                }

            }

        }
    }


