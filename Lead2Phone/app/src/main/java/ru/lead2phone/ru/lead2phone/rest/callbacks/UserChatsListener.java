package ru.lead2phone.ru.lead2phone.rest.callbacks;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 8/18/15.
 */
public interface UserChatsListener {

    void onUsersChatsReceived(String model, Response response);

    void onUserChatsFailed(RetrofitError error);
}
