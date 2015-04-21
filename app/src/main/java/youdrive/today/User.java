package youdrive.today;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class User {

    String session_id;
    String name;
    String avatar;

    @Override
    public String toString() {
        return "User{" +
                "session_id='" + session_id + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
