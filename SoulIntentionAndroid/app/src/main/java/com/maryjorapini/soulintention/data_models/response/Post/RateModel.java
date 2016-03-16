package com.maryjorapini.soulintention.data_models.response.Post;

import java.io.Serializable;

/**
 * Created by Администратор on 12.01.2015.
 */
public class RateModel implements Serializable{
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
