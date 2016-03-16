package com.vakoms.meshly.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.vakoms.meshly.models.job.JobExperience;
import com.vakoms.meshly.constants.Constants;
import com.vakoms.meshly.utils.GeocoderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taras.melko on 8/19/14.
 */
@SuppressWarnings("unused")
public class User implements Comparable, Parcelable {

    private String summary;
    private String summaryTimestamp;
    private List<String> skills;
    private String companyDescription;
    private String companyWebsite;
    private String lastSeen;
    private List<Double> geo;
    private String androidToken;
    private  List<String> followedBy;
    private Privacy privacy;
    @com.google.gson.annotations.SerializedName("image")
    private String imageBase64;
    private String job;
    private String id;
    private String updatedAt;
    private String username;
    private String homeCity;
    private String email;
    private String createdAt;
    private String company;
    private List<String> industries;
    private String apnsToken;
    private String gravatarUrl;
    @com.google.gson.annotations.SerializedName("picture-url")
    private String picture;
    private Follow follow;
    private List<Passport> passports;

    private String interests;
    private String linkedin_id;
    @com.google.gson.annotations.SerializedName("job-experience")
    private List<JobExperience> jobexprience;
    private String status;
    private LinkedInToken linkedin;







    public long getSummaryTimestamp() {
        if(summaryTimestamp == null)return 0;
        return Long.parseLong(summaryTimestamp);
    }

    public void setSummaryTimestamp(long summaryTimestamp) {
        this.summaryTimestamp = String.valueOf(summaryTimestamp);
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public String getLastSeen() {

        if(lastSeen == null){
            lastSeen = "0";
        }
        return lastSeen;
    }

    public List<Double> getGeo() {
        return geo;
    }

    public String getAndroidToken() {
        return androidToken;
    }

    public List<String> getFollowedBy() {
        return followedBy;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public String getJob() {
        return job;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCompany() {
        return company;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public String getApnsToken() {
        return apnsToken;
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public String getPicture() {
        return picture;
    }

    public Follow getFollow() {
        return follow;
    }

    public List<Passport> getPassports() {
        return passports;
    }

    public String getInterests() {
        return interests;
    }

    public String getLinkedin_id() {
        return linkedin_id;
    }

    public List<JobExperience> getJobexprience() {
        return jobexprience;
    }

    public String getStatus() {
        return status;
    }

    public LinkedInToken getLinkedin() {
        return linkedin;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }



    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public void setAndroidToken(String androidToken) {
        this.androidToken = androidToken;
    }

    public void setFollowedBy(List<String> followedBy) {
        this.followedBy = followedBy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public void setApnsToken(String apnsToken) {
        this.apnsToken = apnsToken;
    }

    public void setGravatarUrl(String gravatarUrl) {
        this.gravatarUrl = gravatarUrl;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setFollow(Follow follow) {
        this.follow = follow;
    }

    public void setPassports(List<Passport> passports) {
        this.passports = passports;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedin_id = linkedin_id;
    }

    public void setJobexprience(List<JobExperience> jobexprience) {
        this.jobexprience = jobexprience;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLinkedin(LinkedInToken linkedin) {
        this.linkedin = linkedin;
    }


// methods




   public  void setLastSeen(String _lastSeen) {

        lastSeen = _lastSeen;
    }

    public boolean isMessageProtected() {
        boolean isProtected = false;
        if (getPrivacy() != null && getPrivacy().getChat() != null) {
            if (getPrivacy().getChat().equals(Constants.privacyCasesForRequest[0])) {
                isProtected = false;
            } else if (getPrivacy().getChat().equals(Constants.privacyCasesForRequest[1])) {
                //isProtected = !doesUserFollowMe(this);
            } else if (getPrivacy().getChat().equals(Constants.privacyCasesForRequest[2])) {
                isProtected = true;
            }
        }
        return isProtected;
    }


    @Override
    public String toString() {
        return "User{" +
                "summary='" + summary + '\'' +
                ", skills=" + skills +
                ", companyDescription='" + companyDescription + '\'' +
                ", companyWebsite='" + companyWebsite + '\'' +
                ", lastSeen='" + lastSeen + '\'' +
                ", geo=" + geo +
                ", androidToken='" + androidToken + '\'' +
                ", followedBy=" + followedBy +
                ", privacy=" + privacy +
                ", imageBase64='" + imageBase64 + '\'' +
                ", job='" + job + '\'' +
                ", id='" + id + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", username='" + username + '\'' +
                ", homeCity='" + homeCity + '\'' +
                ", email='" + email + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", company='" + company + '\'' +
                ", industries=" + industries +
                ", apnsToken='" + apnsToken + '\'' +
                ", gravatarUrl='" + gravatarUrl + '\'' +
                ", picture='" + picture + '\'' +
                ", follow=" + follow +
                ", passports=" + passports +
                ", interests='" + interests + '\'' +
                ", linkedin_id='" + linkedin_id + '\'' +
                ", jobexprience=" + jobexprience +
                ", status='" + status + '\'' +
                ", linkedin=" + linkedin +
                '}';
    }

    @Override
    public int compareTo(Object another) {
        User anotherUser = (User) another;

        return (int) GeocoderUtil.getDistance(
                getGeo().get(1),
                getGeo().get(0),
                anotherUser.getGeo().get(1),
                anotherUser.getGeo().get(0));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeString(this.summaryTimestamp);
        dest.writeList(this.skills);
        dest.writeString(this.companyDescription);
        dest.writeString(this.companyWebsite);
        dest.writeString(this.lastSeen);
        dest.writeList(this.geo);
        dest.writeString(this.androidToken);
        dest.writeList(this.followedBy);
        //dest.writeParcelable(this.privacy, 0);
        dest.writeString(this.imageBase64);
        dest.writeString(this.job);
        dest.writeString(this.id);
        dest.writeString(this.updatedAt);
        dest.writeString(this.username);
        dest.writeString(this.homeCity);
        dest.writeString(this.email);
        dest.writeString(this.createdAt);
        dest.writeString(this.company);
        dest.writeList(this.industries);
        dest.writeString(this.apnsToken);
        dest.writeString(this.gravatarUrl);
        dest.writeString(this.picture);
        //dest.writeParcelable(this.follow, 0);
        dest.writeTypedList(passports);
        dest.writeString(this.interests);
        dest.writeString(this.linkedin_id);
        dest.writeTypedList(jobexprience);
        dest.writeString(this.status);
        //dest.writeParcelable(this.linkedin, flags);
    }

    public User() {
    }

    private User(Parcel in) {
        this.summary = in.readString();
        this.summaryTimestamp = in.readString();
        this.skills = new ArrayList<>();
                in.readList(this.skills,null);
        this.companyDescription = in.readString();
        this.companyWebsite = in.readString();
        this.lastSeen = in.readString();
        this.geo = new ArrayList<>();
        in.readList(this.geo, null);
        this.androidToken = in.readString();
        this.followedBy = new ArrayList<>();
        in.readList(this.followedBy, null);
        this.privacy = in.readParcelable(Privacy.class.getClassLoader());
        this.imageBase64 = in.readString();
        this.job = in.readString();
        this.id = in.readString();
        this.updatedAt = in.readString();
        this.username = in.readString();
        this.homeCity = in.readString();
        this.email = in.readString();
        this.createdAt = in.readString();
        this.company = in.readString();
        this.industries =  new ArrayList<>();
                in.readList(this.industries,null);
        this.apnsToken = in.readString();
        this.gravatarUrl = in.readString();
        this.picture = in.readString();
        this.follow = in.readParcelable(Follow.class.getClassLoader());
        in.readTypedList(passports, Passport.CREATOR);
        this.interests = in.readString();
        this.linkedin_id = in.readString();
        in.readTypedList(jobexprience, JobExperience.CREATOR);
        this.status = in.readString();
        this.linkedin = in.readParcelable(LinkedInToken.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setSummaryTimestamp(String summaryTimestamp) {
        this.summaryTimestamp = summaryTimestamp;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }
}
