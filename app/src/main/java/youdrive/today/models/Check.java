package youdrive.today.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Check implements Parcelable {

    @SerializedName("booking_time_left")
    private int bookingTimeLeft;

    @SerializedName("usage_workday_time")
    private int usageWorkdayTime;

    @SerializedName("usage_workday_cost")
    private long usageWorkdayCost;

    @SerializedName("usage_weekend_time")
    private int usageWeekendTime;

    @SerializedName("usage_weekend_cost")
    private long usageWeekendCost;

    @SerializedName("parking_time")
    private int parkingTime;

    @SerializedName("parking_cost")
    private long parkingCost;

    public int getBookingTimeLeft() {
        return bookingTimeLeft;
    }

    public int getUsageWorkdayTime() {
        return usageWorkdayTime;
    }

    public long getUsageWorkdayCost() {
        return usageWorkdayCost;
    }

    public int getUsageWeekendTime() {
        return usageWeekendTime;
    }

    public long getUsageWeekendCost() {
        return usageWeekendCost;
    }

    public int getParkingTime() {
        return parkingTime;
    }

    public long getParkingCost() {
        return parkingCost;
    }

    protected Check(Parcel in) {
        bookingTimeLeft = in.readInt();
        usageWorkdayTime = in.readInt();
        usageWorkdayCost = in.readLong();
        usageWeekendTime = in.readInt();
        usageWeekendCost = in.readLong();
        parkingTime = in.readInt();
        parkingCost = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookingTimeLeft);
        dest.writeInt(usageWorkdayTime);
        dest.writeLong(usageWorkdayCost);
        dest.writeInt(usageWeekendTime);
        dest.writeLong(usageWeekendCost);
        dest.writeInt(parkingTime);
        dest.writeLong(parkingCost);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Check> CREATOR = new Parcelable.Creator<Check>() {
        @Override
        public Check createFromParcel(Parcel in) {
            return new Check(in);
        }

        @Override
        public Check[] newArray(int size) {
            return new Check[size];
        }
    };
}