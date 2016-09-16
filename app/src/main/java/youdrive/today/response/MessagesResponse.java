package youdrive.today.response;

import java.util.List;

import youdrive.today.models.Message;

/**
 * Created by Sergio on 16/09/16.
 */
public class MessagesResponse extends BaseResponse {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }
}
