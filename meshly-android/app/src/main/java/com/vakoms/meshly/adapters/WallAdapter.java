package com.vakoms.meshly.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;

import com.vakoms.meshly.BaseHolder;
import com.vakoms.meshly.databases.UserDAO;
import com.vakoms.meshly.interfaces.OnWallItemListener;
import com.vakoms.meshly.interfaces.PeopleListener;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.models.wall.WallModel;
import com.vakoms.meshly.views.SwipeView;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.R;
import meshly.vakoms.com.meshly.databinding.ItemWallBinding;
import meshly.vakoms.com.meshly.databinding.PeopleItem;

/**
 * Created by Oleh Makhobey on 24.04.2015.
 * tajcig@ya.ru
 */
public class WallAdapter extends RecyclerView.Adapter<BaseHolder> {

    private OnWallItemListener mListener;
    private List<WallModel> mList;



    private int  mImageResource;
    private String mButtonDescription;

    public WallAdapter( int imageResource, String buttonDescription, OnWallItemListener _listener) {
        this.mImageResource = imageResource;
        this.mButtonDescription = buttonDescription;
        this.mListener = _listener;
        this.mList = new ArrayList<>();

    }


    public List<WallModel> getList() {
        return mList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
                case BaseHolder.TYPE_ITEM:
                    ItemWallBinding b =ItemWallBinding.inflate(inflater);
                    return new ViewHolder(b.getRoot(),BaseHolder.TYPE_ITEM);


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
                WallModel model= mList.get(position -1);
                viewHolder.b.setItem(model);
                setSwipeView((SwipeView)viewHolder.b.getRoot(),position,(SwipeView.SwipeOnItemClickListener)holder);
                break;
        }


    }




    private void setSwipeView(SwipeView _view, int _position ,SwipeView.SwipeOnItemClickListener listener){
        _view.setPosition(_position);
        _view.close();
        _view.setSwipeOnItemClickListener(listener);
        _view.setButtonImage(mImageResource);
        _view.setButtonDescription(mButtonDescription);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return BaseHolder.TYPE_HEADER;

        return BaseHolder.TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }



    public class ViewHolder extends BaseHolder implements SwipeView.SwipeOnItemClickListener {
        ItemWallBinding b;

        public ViewHolder(View view, int type) {
            super(view, type);
            b = DataBindingUtil.bind(view);

        }

        @Override
        public void onItemClick(String _tag, int position) {


            switch (_tag) {
                case SwipeView.BUTTON:
                    mListener.onButtonClicked(b.getItem());
                    break;
                case SwipeView.ITEM:
                   mListener.onItemClicked(b.getItem());
                    break;
                case SwipeView.PICTURE:
                    mListener.onPictureClicked(b.getItem().getUser().getId());
                    break;

            }
        }
    }


    public class HeaderHolder extends BaseHolder {

        public HeaderHolder(View itemView ,int type) {
            super(itemView,type);
        }
    }

}





