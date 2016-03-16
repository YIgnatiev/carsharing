package youdrive.today.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psuhoterin on 24.05.15.
 */
public class LoginResponse extends BaseResponse {

    @SerializedName("session_id")
    String sessionId;
    String name;
    String avatar;

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
