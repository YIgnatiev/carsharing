package youdrive.today.models;

/**
 * Created by Sergio on 16/09/16.
 */
public class Message {
    private String message_id;
    private String created_at;
    private String title;
    private String body;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getCreated_at() {
        return created_at;
    }
}
