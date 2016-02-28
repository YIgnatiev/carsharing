package com.vakoms.meshly.interfaces;

import com.vakoms.meshly.models.wall.WallModel;

/**
 * Created by Oleh Makhobey on 19.10.2015.
 * tajcig@ya.ru
 */
public interface OnWallItemListener {


    void onButtonClicked(WallModel model);

    void onItemClicked(WallModel model);

    void onPictureClicked(String userId);
}
