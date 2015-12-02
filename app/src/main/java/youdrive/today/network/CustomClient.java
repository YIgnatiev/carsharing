package youdrive.today.network;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/5/15.
 */
public class CustomClient  extends OkClient {


        CustomClient (OkHttpClient client){
            super(client);
        }

        @Override public Response execute(Request request) throws IOException {
            Response originalRespone = super.execute(request);

            int statusCode = 0;

            if(statusCode == 0) statusCode = 200;
            return new Response(originalRespone.getUrl(),
                    statusCode,
                    originalRespone.getReason(),
                    originalRespone.getHeaders(),
                    originalRespone.getBody());
        }
}
