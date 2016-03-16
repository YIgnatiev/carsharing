package com.carusselgroup.dwt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Properties implements Parcelable {
    public static final Parcelable.Creator<Properties> CREATOR = new Parcelable.Creator<Properties>() {
        public Properties createFromParcel(Parcel source) {
            return new Properties(source);
        }

        public Properties[] newArray(int size) {
            return new Properties[size];
        }
    };
    public int id;
    public String title;

    public Properties() {
    }

    private Properties(Parcel in) {
        this.title = in.readString();
        this.id = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.id);
    }
}
