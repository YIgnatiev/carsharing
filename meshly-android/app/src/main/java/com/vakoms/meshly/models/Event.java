package com.vakoms.meshly.models;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vakoms.meshly.databases.ListRow;
import com.vakoms.meshly.utils.GeocoderUtil;
import com.vakoms.meshly.utils.TimeFormatterUtil;

import java.util.List;

/**
 * Created by taras.melko on 9/5/14.
 */
public class Event {


    public String city;
    public String createdAt;
    public long endTime;
    public String image;
    public String name;
    public long startTime;
    public boolean approved;
    @ListRow
    public List<String> subscribers;
    public String summary;
    public String updatedAt;
    public String url;
    public String id;
    @ListRow
    public List<Double> geo;
    public int subscribersNumber;
    public String address;

    public boolean areWeBothAttendingTheSameEvent;

    public boolean isAreWeBothAttendingTheSameEvent() {
        return areWeBothAttendingTheSameEvent;
    }

    public void setAreWeBothAttendingTheSameEvent(boolean areWeBothAttendingTheSameEvent) {
        this.areWeBothAttendingTheSameEvent = areWeBothAttendingTheSameEvent;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getGeo() {
        return geo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id.equals(event.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setGeo(List<Double> geo) {
        this.geo = geo;
    }

    public int getSubscribersNumber() {
        return subscribersNumber;
    }

    public void setSubscribersNumber(int subscribersNumber) {
        this.subscribersNumber = subscribersNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @BindingAdapter("bind:imageUrl")
    public static void loadImage(SimpleDraweeView imageView, String v) {
        if (v != null)
            imageView.setImageURI(Uri.parse(v));
    }

    @BindingAdapter("bind:setDate")
    public static void setDate(TextView textView, Event event) {

        if (event == null ||event.startTime == 0 && event.endTime == 0) textView.setText("Unknown");
        else {
            String start = TimeFormatterUtil.format((event.startTime * 1000), TimeFormatterUtil.MONTH_DAY);
            String end = TimeFormatterUtil.format((event.endTime * 1000), TimeFormatterUtil.MONTH_DAY);
            textView.setText(start + " - " + end);
        }


    }

    @BindingAdapter("bind:setStartDate")
    public static void setStartDate(TextView textView, Event event) {

        if (event == null ||event.startTime == 0) textView.setText("");
        else {

            String time = TimeFormatterUtil.parseTime(event.startTime);
            //String start = TimeFormatterUtil.format((event.startTime * 1000), TimeFormatterUtil.MONTH_DAY);
            textView.setText(time + " from now");
        }


    }


    public float getDistance(){
        if (geo != null)return GeocoderUtil.getDistacne(geo);
        else return 0;
    }

    @BindingAdapter("bind:setGeo")
    public static void setGeo(TextView textView, Event event) {
        if (event==null || event.getGeo() == null) textView.setText("Unknown");
        else textView.setText(GeocoderUtil.getFormattedDistance(event.geo));
    }


}
