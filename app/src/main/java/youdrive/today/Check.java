package youdrive.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 17.05.15.
 */
public class Check implements Parcelable {

    @SerializedName("booking_time_left")
    private int bookingTimeLeft;

    @SerializedName("usage_workday_time")
    private int usageWorkdayTime;

    @SerializedName("usage_workday_cost")
    private int usageWorkdayCost;

    @SerializedName("usage_weekend_time")
    private int usageWeekendTime;

    @SerializedName("usage_weekend_cost")
    private int usageWeekendCost;

    @SerializedName("parking_time")
    private int parkingTime;

    @SerializedName("parking_cost")
    private int parkingCost;

    private int usageTotal = usageWorkdayCost + usageWeekendCost;

    private int total = usageTotal + parkingCost;

    public int getBookingTimeLeft() {
        return bookingTimeLeft;
    }

    public int getUsageWorkdayTime() {
        return usageWorkdayTime;
    }

    public int getUsageWorkdayCost() {
        return usageWorkdayCost;
    }

    public int getUsageWeekendTime() {
        return usageWeekendTime;
    }

    public int getUsageWeekendCost() {
        return usageWeekendCost;
    }

    public int getParkingTime() {
        return parkingTime;
    }

    public int getParkingCost() {
        return parkingCost;
    }

    public int getUsageTotal() {
        return usageTotal;
    }

    public int getTotal() {
        return total;
    }

    protected Check(Parcel in) {
        parkingCost = in.readInt();
        usageTotal = in.readInt();
        total = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(parkingCost);
        dest.writeInt(usageTotal);
        dest.writeInt(total);
    }

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
