package com.vakoms.meshly.models;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vakoms.meshly.databases.ListRow;
import com.vakoms.meshly.databases.ObjectRow;

import java.util.List;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/23/15.
 */
public class UserMe extends BaseObservable {
    public String summary;
    public String summaryTimestamp;
    public String companyDescription;
    public String companyWebsite;
    public String lastSeen;
    public String androidToken;
    @com.google.gson.annotations.SerializedName("image")
    public String imageBase64;
    public String job;
    public String id;
    public String updatedAt;
    public String username;
    public String homeCity;
    public String email;
    public String createdAt;
    public String company;
    public String apnsToken;
    public String password;
    @com.google.gson.annotations.SerializedName("picture-url")
    public String picture;
    public String interests;
    @com.google.gson.annotations.SerializedName("linkedin_id")

    public String linkedinId;
    public String status;
    @ObjectRow
    public UserToken tokens;


    @ListRow
    public List<Double> geo;
    @ListRow
    public List<String> industries;
    @ListRow
    public List<String> followedBy;
    @ListRow
    public List<String> skills;
    @ListRow
    public List<String>interestedPosts;
    @ObjectRow
    public Privacy privacy;
    @ObjectRow
    public Follow follow;
    @ObjectRow
    public LinkedInToken linkedin;
//    private List<Passport> passports;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryTimestamp() {
        return summaryTimestamp;
    }

    public void setSummaryTimestamp(String summaryTimestamp) {
        this.summaryTimestamp = summaryTimestamp;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getAndroidToken() {
        return androidToken;
    }

    public void setAndroidToken(String androidToken) {
        this.androidToken = androidToken;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getApnsToken() {
        return apnsToken;
    }

    public void setApnsToken(String apnsToken) {
        this.apnsToken = apnsToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLinkedinId() {
        return linkedinId;
    }

    public void setLinkedinId(String linkedinId) {
        this.linkedinId = linkedinId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserToken getTokens() {
        return tokens;
    }

    public void setTokens(UserToken tokens) {
        this.tokens = tokens;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public List<String> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<String> followedBy) {
        this.followedBy = followedBy;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterestedPosts() {
        return interestedPosts;
    }

    public void setInterestedPosts(List<String> interestedPosts) {
        this.interestedPosts = interestedPosts;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Follow getFollow() {
        return follow;
    }

    public void setFollow(Follow follow) {
        this.follow = follow;
    }

    public LinkedInToken getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(LinkedInToken linkedin) {
        this.linkedin = linkedin;
    }




    @BindingAdapter("custom:image")
    public static void load(SimpleDraweeView imageView, String v) {
        imageView.setImageURI(Uri.parse(v));
    }


    public String getIndustriesString(){
        String industriesStr = null;
        if (industries != null) return  industries.toString().replace("[", "").replace("]", "").replace(", ", ",\n");
        else return  "No industries";
    }
}
