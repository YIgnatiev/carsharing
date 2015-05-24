package youdrive.today;

/**
 * Created by psuhoterin on 21.04.15.
 */
public class User {

    String sessionId;
    String name;
    String avatar;

    public User(String sessionId, String name, String avatar) {
        this.sessionId = sessionId;
        this.name = name;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "session_id='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
