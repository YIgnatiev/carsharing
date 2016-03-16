package com.vakoms.meshly.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oleh Makhobey on 21.04.2015.
 * tajcig@ya.ru
 */
public class Passport implements Parcelable {
    private String id;
    private String updatedAt;
    private String protocol;
    private String user;
    private String password;
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedAt() {
        return createdAt;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.updatedAt);
        dest.writeString(this.protocol);
        dest.writeString(this.user);
        dest.writeString(this.password);
        dest.writeString(this.createdAt);
    }

    public Passport() {
    }

    private Passport(Parcel in) {
        this.id = in.readString();
        this.updatedAt = in.readString();
        this.protocol = in.readString();
        this.user = in.readString();
        this.password = in.readString();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<Passport> CREATOR = new Parcelable.Creator<Passport>() {
        public Passport createFromParcel(Parcel source) {
            return new Passport(source);
        }

        public Passport[] newArray(int size) {
            return new Passport[size];
        }
    };
}
