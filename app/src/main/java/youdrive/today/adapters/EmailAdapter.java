package youdrive.today.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import youdrive.today.R;
import youdrive.today.databinding.ItemEmailBinding;
import youdrive.today.models.Invites;

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 28.03.16.
 */
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {
    private List<EmailContact> emailContactList;
    private Set<EmailContact> selectedEmails = new HashSet<>();

    public EmailAdapter(List<EmailContact> emailContactList) {
        this.emailContactList = emailContactList;
    }

    public void setData(List<EmailContact> emailContactList) {
        this.emailContactList = emailContactList;
        selectedEmails.clear();
        notifyDataSetChanged();
    }

    /***
     * Список выбранных компонентов
     */
    public Invites getInvites() {
        return new Invites(new ArrayList<>(selectedEmails));
    }

    @Override
    public EmailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmailBinding listIemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_email, parent, false);
        return new EmailViewHolder(listIemBinding);
    }

    @Override
    public void onBindViewHolder(EmailViewHolder viewHolder, int position) {
        EmailContact emailContact = emailContactList.get(position);

        viewHolder.mLayoutBinding.setEmailContact(emailContact);
        viewHolder.mLayoutBinding.tvCheckBox.setTag(position);
        viewHolder.mLayoutBinding.tvCheckBox.setOnCheckedChangeListener(null);
        viewHolder.mLayoutBinding.tvCheckBox.setChecked(selectedEmails.contains(emailContact));
        viewHolder.mLayoutBinding.tvCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedEmails.add(emailContact);
            else selectedEmails.remove(emailContact);
        });
    }


    @Override
    public int getItemCount() {
        return emailContactList.size();
    }

    public static class EmailViewHolder extends RecyclerView.ViewHolder {
        final ItemEmailBinding mLayoutBinding;

        public EmailViewHolder(ItemEmailBinding layoutBinding) {
            super(layoutBinding.getRoot());
            mLayoutBinding = layoutBinding;
        }
    }

    public static class EmailContact {
        public String name;
        public String email;

        public EmailContact(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}