package youdrive.today.response;

import com.google.gson.annotations.SerializedName;

import youdrive.today.Check;

/**
 * Created by psuhoterin on 24.05.15.
 */
public class CommandResponse extends BaseResponse {

    @SerializedName("result_token")
    String resultToken;
    String status;
    Check check;

    public Check getCheck() {
        return check;
    }

    public String getResultToken() {
        return resultToken;
    }

    public String getStatus() {
        return status;
    }
}
