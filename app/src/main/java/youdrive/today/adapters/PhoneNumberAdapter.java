package youdrive.today.adapters;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import youdrive.today.R;
import youdrive.today.databinding.ItemPhoneInviteBinding;

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 28.03.16.
 */
public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneViewHolder>
{
    private List<PhoneContact> phoneContacts;
    private OnInviteClickListener onInviteClickListener;

    public PhoneNumberAdapter(List<PhoneContact> phoneContacts, OnInviteClickListener onInviteClickListener)
    {
        this.onInviteClickListener=onInviteClickListener;
        this.phoneContacts = phoneContacts;
    }
    public void setData(List<PhoneContact> phoneContacts)
    {
        this.phoneContacts =phoneContacts;
        notifyDataSetChanged();
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPhoneInviteBinding listIemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_phone_invite, parent, false);
        return new PhoneViewHolder(listIemBinding);
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder viewHolder, int position) {
        PhoneContact phoneContact=phoneContacts.get(position);
        viewHolder.mLayoutBinding.setPhoneContact(phoneContact);
        viewHolder.mLayoutBinding.bInviteSMS.setOnClickListener(v -> {
            onInviteClickListener.onClick(phoneContact);
        });
    }


    @Override
    public int getItemCount() {
        return phoneContacts.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder
    {
        final ItemPhoneInviteBinding mLayoutBinding;
        public PhoneViewHolder(ItemPhoneInviteBinding layoutBinding) {
            super(layoutBinding.getRoot());
            mLayoutBinding = layoutBinding;
        }
    }
    public static class PhoneContact
    {
        public String name;
        public String phone;

        public PhoneContact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }
    public interface OnInviteClickListener
    {
        void onClick(PhoneContact phoneContact);
    }
}