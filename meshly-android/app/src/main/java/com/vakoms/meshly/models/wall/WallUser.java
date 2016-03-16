package com.vakoms.meshly.models.wall;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Oleh Makhobey on 30.04.2015.
 * tajcig@ya.ru
 */
public class WallUser  {
    public String id;
    public String name;
    public String job;
    public String avatar;
    public String company;
    public String url;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @BindingAdapter("custom:image")
    public static void load(SimpleDraweeView imageView, String v) {
        if(v!= null)
        imageView.setImageURI(Uri.parse(v));
    }

}
