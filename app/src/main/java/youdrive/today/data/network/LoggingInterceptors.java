package youdrive.today.data.network;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import timber.log.Timber;

/**
 * Created by psuhoterin on 23.04.15.
 */
public class LoggingInterceptors implements Interceptor {

    public LoggingInterceptors() {
        Timber.tag("URLLogging");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Timber.d("REQUEST URL " + request.url().toString());
        Response response = chain.proceed(request);
        return response;
    }
}
