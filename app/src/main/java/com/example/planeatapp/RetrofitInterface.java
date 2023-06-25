package com.example.planeatapp;

import java.util.HashMap;
import java.util.List;
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

    @POST("/prompt")
    Call<Map<String, String>> executePrompt(@Body HashMap<String, String> map);

    @GET("eventCon/{id}")
    Call<List<Confirmation>> executeEventCon(@Path("id") String id);

    @GET("listInfoByEvent/{id}")
    Call<IngredientList> executeListByEventId(@Path("id") String id);
}
