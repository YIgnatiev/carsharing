package ru.lead2phone.ru.lead2phone.rest;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.lead2phone.ru.lead2phone.models.Caller;

/**
 * Created by Oleh Makhobey on 16.07.2015.
 * tajcig@ya.ru
 */
public interface LeadToPhoneService {


    @GET("/l2pcaller.php")
    void getList(
            @Query("callerid") String id,
            @Query("pass") String password,
            @Query("status") int status,
            Callback<List<Caller>> callback);


}


