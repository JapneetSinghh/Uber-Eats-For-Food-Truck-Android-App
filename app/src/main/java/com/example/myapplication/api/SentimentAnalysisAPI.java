package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SentimentAnalysisAPI {

    @Headers("Content-Type: application/json")
    @POST("analyze-sentiment")
    Call<SentimentResponse> analyzeSentiment(@Body SentimentRequest request);

}

