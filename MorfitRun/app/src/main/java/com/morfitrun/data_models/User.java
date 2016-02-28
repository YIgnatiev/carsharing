package com.morfitrun.data_models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 12.03.2015.
 */
public class User implements Parcelable {

    private String fullName;
    private String avatar;
    private String uId;
    private String login;
    private String password;
    private String email;
    private String birthDay;
    private String gender;

    public User(String _email,String _password){
        this.email = _email;
        this.password = _password;
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User (){

    }


    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", uId='" + uId + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.avatar);
        dest.writeString(this.uId);
        dest.writeString(this.login);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.birthDay);
        dest.writeString(this.gender);
    }

    private User(Parcel in) {
        this.fullName = in.readString();
        this.avatar = in.readString();
        this.uId = in.readString();
        this.login = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.birthDay = in.readString();
        this.gender = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
