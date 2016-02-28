package com.vakoms.meshly.models.wall;

import android.databinding.BindingAdapter;
import android.location.Location;
import android.view.View;
import android.widget.TextView;

import com.vakoms.meshly.databases.ListRow;
import com.vakoms.meshly.databases.ObjectRow;
import com.vakoms.meshly.models.AddressResponse;
import com.vakoms.meshly.models.NewUser;
import com.vakoms.meshly.rest.RetrofitApi;
import com.vakoms.meshly.utils.TimeFormatterUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Oleh Makhobey on 30.04.2015.
 * tajcig@ya.ru
 */
public class WallModel {

    public String title;
    public String description;
    @ListRow
    public List<String> industries;
    @ListRow
    public List<String> skills;
    @ListRow
    public List<String> posts;
    public int time;
    public int interval;
    public long expirationDate;
    @ListRow
    public List<Double> geo;
    public boolean isOpened;
    public String id;
    public String createdAt;
    public String updateAt;
    @ObjectRow
    public WallUser user;
    public String lng;
    public String lat;

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setInterval(int _interval){
        this.interval = _interval;
    }

    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIndustries() {
        return industries;
    }

    public void setIndustries(List<String> industries) {
        this.industries = industries;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Double> getGeo() {

        return geo;
    }

    public void setGeo(ArrayList<Double> geo) {

        this.geo = geo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public WallUser getUser() {
        return user;
    }

    public void setUser(WallUser user) {
        this.user = user;
    }



    @BindingAdapter("bind:setTime")
    public static void setTime(TextView textView,WallModel model) {

        if(model.expirationDate!= 0) {

            textView.setText(TimeFormatterUtil.parseTime(model.expirationDate ));
        }else textView.setText("Active");

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
                    .subscribe(address -> onLocationSuccess(address, textView), WallModel::onLocationError);

        }


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
