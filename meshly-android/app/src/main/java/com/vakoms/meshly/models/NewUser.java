package com.vakoms.meshly.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.location.Location;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vakoms.meshly.databases.ListRow;
import com.vakoms.meshly.databases.ObjectRow;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.GeocoderUtil;
import com.vakoms.meshly.utils.TimeCounterUtil;
import com.vakoms.meshly.utils.TimeFormatterUtil;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.BR;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/11/15.
 */
public class NewUser extends BaseObservable{
    @com.google.gson.annotations.SerializedName("_id")
    public String newId;
    public String id;
    public String attendingTheSameEvent;
    public String fromTheSameCountry;
    public String fromTheSameCompany;
    public String fromTheSameIndustries;
    public String theSameOpportunitiesInterested;
    public String job;
    @com.google.gson.annotations.SerializedName("picture-url")
    public String picture;
    public String summary;
    public String username;
    public String homeCity;
    public String formattedWhatIsInCommon;
    public String lastSeenFormatter;
    public String lastSeenString;
    public String company;
    public String companyDescription;
    public String companyWebsite;
    @com.google.gson.annotations.SerializedName("linkedin_id")

    public String linkedinId;
    public int createdOpportunities;
    public int followsCount;
    public int followersCount;
    public String lastSeen;
    public String androidToken;



    public boolean isFollowedByMe;
    @ListRow
    public List<String> skills;
    @ListRow
    public List<String> industries;
    @ListRow
    public List<Double> geo;
    @ListRow
    public List<String>interestedPosts;
    //public ArrayList<Passport> passports;
    @ObjectRow
    public Privacy privacy;
    @ObjectRow
    public LinkedInToken linkedin;





    public String getAndroidToken() {
        return androidToken;
    }

    public void setAndroidToken(String androidToken) {
        this.androidToken = androidToken;
    }


    public List<String> getInterestedPosts() {
        return interestedPosts;
    }

    public void setInterestedPosts(List<String> interestedPosts) {
        this.interestedPosts = interestedPosts;
    }

    public int getCreatedOpportunities() {
        return createdOpportunities;
    }
    @Bindable
    public String getHomeCity() {
        if(homeCity != null && homeCity.isEmpty())return null;
        return homeCity;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Bindable
    public String getLastSeenString() {
        return lastSeenString;
    }

    public void setLastSeenString(String lastSeenString) {
        this.lastSeenString = lastSeenString;
        notifyPropertyChanged(BR.lastSeenString);

    }

    public String getFormattedWhatIsInCommon() {
        return formattedWhatIsInCommon;
    }

    public String getLastSeenFormatter() {
        return lastSeenFormatter;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getCompanyDescription() {
        if(companyDescription != null && companyDescription.isEmpty())return null;
        return companyDescription;
    }

    public String getCompanyWebsite() {
        if(companyWebsite != null && companyWebsite.isEmpty())return null;
        return companyWebsite;
    }

    public String getLinkedin_id() {
        return linkedinId;
    }
    @Bindable
    public boolean getIsFollowedByMe() {
        return isFollowedByMe;
    }

    public void setIsFollowedByMe(boolean isFollowedByMe) {
        this.isFollowedByMe = isFollowedByMe;
        notifyPropertyChanged(BR.isFollowedByMe);
    }

    public int getFollowsCount() {
        return followsCount;
    }

    public String getAttendingTheSameEvent() {
        return attendingTheSameEvent;
    }

    public String getFromTheSameCountry() {
        return fromTheSameCountry;
    }

    public String getFromTheSameCompany() {
        return fromTheSameCompany;
    }

    public String getFromTheSameIndustries() {
        return fromTheSameIndustries;
    }

    public String getTheSameOpportunitiesInterested() {
        return theSameOpportunitiesInterested;
    }


    public String getCompany() {
        if(company != null && company.isEmpty())return null;
        return company;
    }

    public ArrayList<Double> getGeo() {
        return (ArrayList)geo;
    }

    public String getJob() {
        return job;
    }



    public String getPicture() {
        return picture;
    }

    public ArrayList<String> getSkills() {
        return (ArrayList)skills;
    }

    public String getSummary() {
        return summary;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id == null ? newId : id;
    }

    //public ArrayList<Passport> getPassports() {
     //   return passports;
   // }

    public ArrayList<String> getIndustries() {

        if(industries != null && industries.isEmpty())return null;

        return (ArrayList)industries;
    }




    public void setId(String id) {
        this.id = id;
    }

    public void setAttendingTheSameEvent(String attendingTheSameEvent) {
        this.attendingTheSameEvent = attendingTheSameEvent;
    }

    public void setFromTheSameCountry(String fromTheSameCountry) {
        this.fromTheSameCountry = fromTheSameCountry;
    }

    public void setFromTheSameCompany(String fromTheSameCompany) {
        this.fromTheSameCompany = fromTheSameCompany;
    }

    public void setFromTheSameIndustries(String fromTheSameIndustries) {
        this.fromTheSameIndustries = fromTheSameIndustries;
    }

    public void setTheSameOpportunitiesInterested(String theSameOpportunitiesInterested) {
        this.theSameOpportunitiesInterested = theSameOpportunitiesInterested;
    }

//    public void setPassports(ArrayList<Passport> passports) {
//        this.passports = passports;
//    }

    public void setGeo(ArrayList<Double> geo) {
        this.geo = geo;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public void setIndustries(ArrayList<String> industries) {
        this.industries = industries;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public void setCreatedOpportunities(int createdOpportunities) {
        this.createdOpportunities = createdOpportunities;
    }

    public void setFollowsCount(int followsCount) {
        this.followsCount = followsCount;
    }

    public void setFormattedWhatIsInCommon(String formattedWhatIsInCommon) {
        this.formattedWhatIsInCommon = formattedWhatIsInCommon;
    }

    public void setLastSeenFormatter(String lastSeenFormatter) {
        this.lastSeenFormatter = lastSeenFormatter;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedinId = linkedin_id;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }



    @BindingAdapter("bind:setTime")
    public static void setTime(TextView textView,NewUser user) {

        if(user != null && user.lastSeen != null && !user.lastSeen.isEmpty()) {
            long curTime = Long.parseLong(user.lastSeen.replace(".", "").substring(0, 10));
           textView.setText(TimeFormatterUtil.parseTime(curTime) + " ago");
        }else textView.setVisibility(View.GONE);

    }


    @BindingAdapter("bind:imageUrl")
    public static void loadImage(SimpleDraweeView imageView, String v) {
        if(v!= null)
        imageView.setImageURI(Uri.parse(v));
    }

    @BindingAdapter("bind:setLocation")
    public static void loadImage(TextView textView, List<Double> coordinates) {

        if(coordinates!=null){

            Location location = new Location("");
            location.setLatitude(coordinates.get(1));
            location.setLongitude(coordinates.get(0));




            RetrofitApi.getInstance().meshly()
                    .getLocationAddress(location)
                     .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(address -> onLocationSuccess(address, textView), NewUser::onLocationError);

        }


    }


 @BindingAdapter("bind:setDistance")
    public static void setDistance(TextView textView, List<Double> coordinates) {
    if(coordinates == null || coordinates.isEmpty() || coordinates.get(0) == 0){
        textView.setVisibility(View.GONE);
    }else{
        String  distance =  GeocoderUtil.getFormattedDistance(coordinates);

        textView.setText(distance);
    }
 }


    @BindingAdapter("bind:setJob")
    public static void setJob(TextView textView, NewUser user) {

        String job = "";
        if(user != null && user.job != null )job =  user.job ;
        if(user != null && user.job != null && user.company != null) job += " at " + user.company;

        textView.setText(job);
 }

    private static void onLocationError(Throwable throwable){

    }

    private static  void onLocationSuccess(AddressResponse address , TextView textView ){




        if(address.getAddress() == null || address.getAddress().getCountry()==null)textView.setVisibility(View.GONE);
        String city = address.getAddress().getCity() != null ? ", " + address.getAddress().getCity() : "";
        String addressStr =address.getAddress().getCountry() + city;

        textView.setText(addressStr);

    }








}
