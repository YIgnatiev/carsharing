package youdrive.today.response;

/**
 * Created by psuhoterin on 24.05.15.
 */
public class BaseResponse {

    private boolean success;

    private int code;
    private String text;

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
