package youdrive.today.models;

import android.os.Parcel;
import android.os.Parcelable;

import youdrive.today.response.BaseResponse;

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 29.03.16.
 */
public class ReferralRules extends BaseResponse implements Parcelable
{
    public int max_inviter_bonus;
    public int inviter_bonus_part;
    public int max_invitee_bonus;
    public int invitee_bonus_part;
    public String referral_code;
    public String registration_link;

    protected ReferralRules(Parcel in) {
        max_inviter_bonus = in.readInt();
        inviter_bonus_part = in.readInt();
        max_invitee_bonus = in.readInt();
        invitee_bonus_part = in.readInt();
        referral_code = in.readString();
        registration_link = in.readString();
    }

    public static final Creator<ReferralRules> CREATOR = new Creator<ReferralRules>() {
        @Override
        public ReferralRules createFromParcel(Parcel in) {
            return new ReferralRules(in);
        }

        @Override
        public ReferralRules[] newArray(int size) {
            return new ReferralRules[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(max_inviter_bonus);
        dest.writeInt(inviter_bonus_part);
        dest.writeInt(max_invitee_bonus);
        dest.writeInt(invitee_bonus_part);
        dest.writeString(referral_code);
        dest.writeString(registration_link);
    }

    @Override
    public String toString() {
        return "ReferralRules{" +
                "max_inviter_bonus=" + max_inviter_bonus +
                ", inviter_bonus_part=" + inviter_bonus_part +
                ", max_invitee_bonus=" + max_invitee_bonus +
                ", invitee_bonus_part=" + invitee_bonus_part +
                ", referral_code='" + referral_code + '\'' +
                ", registration_link='" + registration_link + '\'' +
                '}';
    }
}
