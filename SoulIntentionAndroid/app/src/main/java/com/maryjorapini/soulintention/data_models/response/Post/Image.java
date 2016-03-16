package com.maryjorapini.soulintention.data_models.response.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sasha on 20.11.2014.
 */
public class Image implements Serializable {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    /**
     * @return The postId
     */
    public Integer getPostId() {
        return postId;
    }

    /**
     * @param postId The post_id
     */
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    /**
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

