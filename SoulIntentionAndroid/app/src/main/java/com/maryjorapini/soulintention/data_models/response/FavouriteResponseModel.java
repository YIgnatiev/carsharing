package com.maryjorapini.soulintention.data_models.response;

import com.maryjorapini.soulintention.data_models.response.Post.PostResponseModel;

/**
 * Created by Sasha on 20.11.2014.
 */
public class FavouriteResponseModel  extends SessionMobileResponseModel{
    private int id;
    private int mobile_user_id;
    private int post_id;
    private String created_at;
    private String updated_at;
    private PostResponseModel post;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMobile_user_id() {
        return mobile_user_id;
    }

    public void setMobile_user_id(int mobile_user_id) {
        this.mobile_user_id = mobile_user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public PostResponseModel getPost() {
        return post;
    }

    public void setPost(PostResponseModel post) {
        this.post = post;
    }
}
