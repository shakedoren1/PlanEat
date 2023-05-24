package com.example.planeatapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/newEvent")
    Call<Void> executeNewEvent(@Body HashMap<String, String> map);

    @POST("/eventDetails")
    Call<EventDetails> executeEventDetails(@Body HashMap<String, String> map);
}
