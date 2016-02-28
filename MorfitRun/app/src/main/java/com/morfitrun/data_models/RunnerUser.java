package com.morfitrun.data_models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 24.03.2015.
 */
public class RunnerUser implements Parcelable {

    private String fullName;
    private String avatar;
    private String email;
    private String birthDate;
    private String gender;
    private String _id;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.avatar);
        dest.writeString(this.email);
        dest.writeString(this.birthDate);
        dest.writeString(this.gender);
        dest.writeString(this._id);
    }

    public RunnerUser() {
    }

    private RunnerUser(Parcel in) {
        this.fullName = in.readString();
        this.avatar = in.readString();
        this.email = in.readString();
        this.birthDate = in.readString();
        this.gender = in.readString();
        this._id = in.readString();
    }

    public static final Parcelable.Creator<RunnerUser> CREATOR = new Parcelable.Creator<RunnerUser>() {
        public RunnerUser createFromParcel(Parcel source) {
            return new RunnerUser(source);
        }

        public RunnerUser[] newArray(int size) {
            return new RunnerUser[size];
        }
    };
}
