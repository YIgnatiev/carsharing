package com.carusselgroup.dwt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageCar implements Parcelable {
    public static final Parcelable.Creator<ImageCar> CREATOR = new Parcelable.Creator<ImageCar>() {
        public ImageCar createFromParcel(Parcel source) {
            return new ImageCar(source);
        }

        public ImageCar[] newArray(int size) {
            return new ImageCar[size];
        }
    };
    public int id;
    public String origUrl;
    public String thumbnailUrl;
    public String mediumUrl;
    public int orderIndex;

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public String uploadedFilePath = "";

    public ImageCar() {
    }

    private ImageCar(Parcel in) {
        this.id = in.readInt();
        this.origUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.mediumUrl = in.readString();
        this.orderIndex = in.readInt();
        this.uploadedFilePath = in.readString();
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.origUrl);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.mediumUrl);
        dest.writeInt(this.orderIndex);
        dest.writeString(this.uploadedFilePath);
    }
}
