package com.budivnictvo.rssnews.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.budivnictvo.rssnews.app.R;
import com.budivnictvo.rssnews.app.data.RssChannel;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by Администратор on 11.11.2014.
 */
public class ChannelAdapter extends ArrayAdapter<RssChannel>{

    private RssChannel[] mData;
    private Context context;
    private int resource;
    private boolean isInited = false;
    private Holder holder;
    private View mConvertView;
    private TextView tvChannelText;
    private ImageView ivChannelImage;

    public ChannelAdapter(Context context, int resource, RssChannel[] data) {
        super(context,  resource, data);
        this.context = context;
        this.resource = resource;
        this.mData= data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           mConvertView = inflater.inflate(resource, parent, false);

            tvChannelText = (TextView)mConvertView.findViewById(R.id.tvChannelName);
             ivChannelImage = (ImageView)mConvertView.findViewById(R.id.ivChannelLogo);
           setData(mData[position]);





        return mConvertView;
    }
   private void setData(RssChannel _data){
       tvChannelText.setText(_data.getName());
       ivChannelImage.setImageDrawable(context.getResources().getDrawable(_data.getLogoId()));
   }
    private class Holder {
        TextView tvChannelText;
        ImageView ivChannelImage;
        public Holder(View _rootView){

        }
    }
}
