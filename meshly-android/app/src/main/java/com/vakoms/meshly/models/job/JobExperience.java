package com.vakoms.meshly.models.job;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taras.melko on 10/3/14.
 */
@SuppressWarnings("unused")
public class JobExperience implements Parcelable {

    public static final Parcelable.Creator<JobExperience> CREATOR = new Parcelable.Creator<JobExperience>() {
        public JobExperience createFromParcel(Parcel source) {
            return new JobExperience(source);
        }

        public JobExperience[] newArray(int size) {
            return new JobExperience[size];
        }
    };

    private String startDate;
    private String company;
    private String title;
    private String endDate;

    private JobExperience(Parcel in) {
        this.startDate = in.readString();
        this.company = in.readString();
        this.title = in.readString();
        this.endDate = in.readString();
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.company);
        dest.writeString(this.title);
        dest.writeString(this.endDate);
    }
}