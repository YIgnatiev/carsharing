package com.carusselgroup.dwt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.carusselgroup.dwt.R;
import com.carusselgroup.dwt.model.ImageCar;
import com.carusselgroup.dwt.rest.CarsNewApi;
import com.carusselgroup.dwt.rest.IResponse;
import com.carusselgroup.dwt.ui.fragment.CarDetailFragment;
import com.nhaarman.listviewanimations.util.Swappable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class CarPhotosAdapter extends BaseAdapter implements Swappable {

    private ArrayList<ImageCar> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private CarDetailFragment.OnDragListener mDragListener;
    private final int REQUIRED_WIDTH = 600;
    private final int REQUIRED_HEIGHT = 600;


    public CarPhotosAdapter(Context context, ArrayList<ImageCar> data) {
        if (data != null)
            mData = data;
        else
            mData = new ArrayList<>();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmDragListener(CarDetailFragment.OnDragListener mDragListener) {
        this.mDragListener = mDragListener;
    }

    public int addItem(ImageCar photo) {
        mData.add(photo);
        notifyDataSetChanged();
        return mData.size() - 1;
    }


    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ImageCar getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mData.size() > position) {
            return mData.get(position).hashCode();
        } else {
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_car_detail_list_item, parent, false);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.imageview_CarPhoto_CarDetailFragment);
            holder.itemImagePlaceholder = (ImageView) convertView.findViewById(R.id.placeholder_CarPhoto_CarDetailFragment);
            holder.imagePlaceHolder = convertView.findViewById(R.id.layout_ImagePlaceholder_CarDetailFragment);


            convertView.setTag(holder);
        }else {
          holder = (ViewHolder) convertView.getTag();
          holder.itemImage.setImageBitmap(null);


        }
        holder.itemImagePlaceholder.setVisibility(View.VISIBLE);



//        if (!mData.get(position).getUploadedFilePath().isEmpty()){
//            File imageFile = new File(mData.get(position).getUploadedFilePath());
//
////            Bitmap bitmap = decodeBitmap(mData.get(position).getUploadedFilePath() , REQUIRED_WIDTH,REQUIRED_HEIGHT);
////            holder.itemImage.setImageBitmap(bitmap);
//
//
//            Picasso.with(mContext)
//                    .load(imageFile)
//                    .into(holder.itemImage);
//        }
//        else {

            final ImageView placeholder = holder.itemImagePlaceholder;

//            CarsNewApi.getImage(mData.get(position).getOrigUrl(), new IResponse() {
//
//                @Override
//                public void onSuccess(Object response) {
//
//                    placeholder.setImageBitmap((Bitmap) response);
//                    placeholder.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(Object response) {
//                    Toast.makeText(mContext,(String)response, Toast.LENGTH_SHORT).show();
//                }
//            });




            Picasso.with(mContext)
                    .load(mData.get(position).getOrigUrl())
                    .resize(600,600)
                    .centerInside()
                    .into(holder.itemImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            placeholder.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
//        }
        return convertView;
    }

    @Override
    public void swapItems(int a, int b) {
        if (mData != null) {
            if (mDragListener != null)
                mDragListener.OnSwap(a, b);
        }
    }

    public void hardSwap(int a, int b) {
        Collections.swap(mData, a, b);
        notifyDataSetChanged();
    }








    private class ViewHolder {

        ImageView itemImage;
        ImageView itemImagePlaceholder;
        View imagePlaceHolder;
    }

}
