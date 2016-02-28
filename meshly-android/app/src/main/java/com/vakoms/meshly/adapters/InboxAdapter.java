package com.vakoms.meshly.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vakoms.meshly.interfaces.InboxItemListener;
import com.vakoms.meshly.interfaces.MessageItemListener;
import com.vakoms.meshly.models.chat.MessageBody;

import java.util.List;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ItemChatBinding;

/**
 * Created by taras.teslyuk on 15.09.2014.
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    private String myId;
    private List<MessageBody> mList;
    private LayoutInflater mInflater;

    private MessageItemListener mListener;

    public InboxAdapter(Context context, List<MessageBody> list ,String myId , MessageItemListener listener) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
        this.myId = myId;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemChatBinding b = DataBindingUtil.inflate(mInflater, R.layout.item_chat, parent, false);

        return new ViewHolder(b.getRoot());
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ViewHolder holder , int position) {

        MessageBody message =mList.get(position);
        holder.b.setMessage(message);
        holder.b.setIsMy(message.getUser().getId().equals(myId));

        holder.b.setListener(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements InboxItemListener {
        ItemChatBinding b;
        public ViewHolder(View view) {


            super(view);
            b = DataBindingUtil.bind(view);
        }

        @Override
        public void onMessageClick(View view) {
            int position  = getAdapterPosition();
            mListener.onMessageClick(mList.get(position));


        }

        @Override
        public void onUserClick(View view) {

            int position = getAdapterPosition();

            mListener.onUserClick(mList.get(position).getUser().getId());

        }
    }



}


















































//
//    public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
//
//        private PeopleItemListener mListener;
//        public List<NewUser> mList ;
//        public PeopleAdapter(List<NewUser> users, PeopleItemListener listener){
//            this.mList = users;
//            this.mListener = listener;
//        }
//
//
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view  = PeopleItem.inflate(inflater, parent, false).getRoot();
//
//            return new ViewHolder(view);
//        }
//
//
//
//
//        @Override
//        public void onBindViewHolder(PeopleAdapter.ViewHolder holder , int position) {
//
//            NewUser user =mList.get(position);
//            holder.b.setUser(user);
//            holder.b.setListener(holder);
//
//            if(holder.getAdapterPosition() ==0) holder.b.setIsFirst(true);
//            else holder.b.setIsFirst(false);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList.size();
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder implements PeopleListener {
//            PeopleItem b;
//            public ViewHolder(View view) {
//
//
//                super(view);
//                b = DataBindingUtil.bind(view);
//
//
//
//            }
//
//            @Override
//            public void onFollow(View view) {
//                mListener.onFollow(b.getUser().getId());
//            }
//
//            @Override
//            public void onChat(View view) {
//                mListener.onChat(b.getUser().getId());
//            }
//
//            @Override
//            public void onEvent(View view) {
//                //TODO make event
//            }
//
//            @Override
//            public void onProfile(View view) {
//                mListener.onProfile(b.ivUserAvatarItemPeople ,b.getUser());
//            }
//        }
