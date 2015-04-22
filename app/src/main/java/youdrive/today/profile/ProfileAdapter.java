package youdrive.today.profile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import youdrive.today.Item;
import youdrive.today.R;

/**
 * Created by psuhoterin on 17.04.15.
 */
public class ProfileAdapter extends ArrayAdapter<Item> {

    private final Context mContext;
    private final int mResource;
    private final List<Item> mObjects;

    public ProfileAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);

            holder = new ItemHolder(row);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        Item item = mObjects.get(position);
        holder.txtTitle.setText(item.title);
        holder.ivIcon.setImageResource(item.icon);

        return row;
    }

    static class ItemHolder {

        @InjectView(R.id.txtTitle)
        TextView txtTitle;
        @InjectView(R.id.ivIcon)
        ImageView ivIcon;

        public ItemHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}


