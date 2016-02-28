package com.maryjorapini.soulintention.data_models.response;

import java.io.Serializable;

/**
 * Created by Sasha on 12.01.2015.
 */
public class Rate implements Serializable{
    private int postId;
    private int rate;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getRate() {

        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
