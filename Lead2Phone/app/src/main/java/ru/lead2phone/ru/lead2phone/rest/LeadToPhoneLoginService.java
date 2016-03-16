package ru.lead2phone.ru.lead2phone.rest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/10/15.
 */
public interface LeadToPhoneLoginService {


    @GET("/l2plogin.php")
    void login(
            @Query("callerid") String id,
            @Query("pass") String password,
            Callback<String> callback);

    @GET("/l2pdelete.php")
    void deleteUser( @Query("callerid") String id,
                     @Query("pass") String password,
                     @Query("callid")String callId,
                     Callback<String> callback);


    @GET("/l2plater.php")
    void callLater( @Query("callerid") String id,
                    @Query("pass") String password,
                    @Query("callid")String callId,
                    Callback<String> callback);

    @GET("/l2about.php")
    void about (Callback<String> callback);

}
