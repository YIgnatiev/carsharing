package com.maryjorapini.soulintention.data_models.response.Post;

import com.google.gson.annotations.SerializedName;
import com.maryjorapini.soulintention.data_models.response.SessionMobileResponseModel;

/**
 * Created by Sasha on 20.11.2014.
 */
public class AboutAuthorModel extends SessionMobileResponseModel {
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("about_info")
    private String aboutInfo;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAboutInfo() {
        return aboutInfo;
    }

    public void setAboutInfo(String aboutInfo) {
        this.aboutInfo = aboutInfo;
    }
}
