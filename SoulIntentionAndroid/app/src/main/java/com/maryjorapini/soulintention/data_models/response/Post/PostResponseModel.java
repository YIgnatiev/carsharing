package com.maryjorapini.soulintention.data_models.response.Post;

/**
 * Created by Sasha on 20.11.2014.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.maryjorapini.soulintention.custom_views.swipe_item.StateItem;
import com.maryjorapini.soulintention.data_models.response.Author;
import com.maryjorapini.soulintention.data_models.response.Rate;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;

import java.util.ArrayList;
import java.util.List;

public class PostResponseModel extends SessionMobileResponseModel {

    public StateItem stateItem          = StateItem.IDLE;
    public boolean isLiked              = false;

    public boolean isFacebookPressed    = false;
    public boolean isTwitterPressed     = false;



    @Expose
    private Integer id;
    @Expose
    private String title;
    @Expose
    private String details;
    @SerializedName("author_id")
    @Expose
    private Integer authorId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @Expose
    private Author author;
    @Expose
    private Rate rate;
    @Expose
    private String post_on_date;

    @Expose
    private boolean favourite;
    @Expose
    private List<Image> images = new ArrayList<Image>();

    public PostResponseModel(String title, String details, String updatedAt) {
        this.title = title;
        this.details = details;
        this.updatedAt = updatedAt;
        this.stateItem = StateItem.IDLE;
    }
    public PostResponseModel(){

    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The details
     */
    public String getDetails() {
        return details;
    }

    /**
     *
     * @param details
     * The details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     *
     * @return
     * The authorId
     */
    public Integer getAuthorId() {
        return authorId;
    }

    /**
     *
     * @param authorId
     * The author_id
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     *
     * @param images
     * The images
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getPost_on_date() {
        return post_on_date;
    }

    public void setPost_on_date(String post_on_date) {
        this.post_on_date = post_on_date;
    }
}