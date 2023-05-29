package com.example.planeatapp;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @POST("/newEvent")
    Call<Map<String, String>> executeNewEvent(@Body HashMap<String, String> map);

    @GET("eventInfo/{id}")
    Call<EventDetails> executeEventInfo(@Path("id") String id);
}
