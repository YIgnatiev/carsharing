package youdrive.today;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class User implements Parcelable {

    private String sessionId;
    private String name;
    private String avatar;

    public User(String sessionId, String name, String avatar) {
        this.sessionId = sessionId;
        this.name = name;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "session_id='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    protected User(Parcel in) {
        sessionId = in.readString();
        name = in.readString();
        avatar = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sessionId);
        dest.writeString(name);
        dest.writeString(avatar);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
