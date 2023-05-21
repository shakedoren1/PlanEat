package com.example.planeatapp;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/newEvent")
    Call<Map<String, String>> executeNewEvent(@Body HashMap<String, String> map);

    @POST("/eventDetails")
    Call<EventDetails> executeEventDetails(@Body HashMap<String, String> map);
}
