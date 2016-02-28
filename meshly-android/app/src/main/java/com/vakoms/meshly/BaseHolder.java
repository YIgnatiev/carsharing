package com.vakoms.meshly;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Oleh Makhobey on 16.10.2015.
 * tajcig@ya.ru
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int itemType ;

    public BaseHolder(View view , int type){
        super(view);
        this.itemType = type;
    }

    public int getItemType(){
        return itemType;
    }


}
