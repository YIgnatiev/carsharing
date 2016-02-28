package pro.theboard.rest;

import pro.theboard.constants.Constants;
import retrofit.RequestInterceptor;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class RequestHeaders implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {

        request.addHeader(Constants.CONTENT_TYPE, Constants.URL_ENCODED);
        request.addHeader(Constants.X_BOARD_APP_ID, Constants.API_KEY);
    }
}
