package com.example.filmesfamosos.service;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.model.ServiceResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by diegocotta on 30/09/2018.
 */
//Reference https://zeroturnaround.com/rebellabs/getting-started-with-retrofit-2/
public interface Service {


    @GET("movie/popular")
    Call<ServiceResult> callMostPopular(@Query("api_key") String key, @Query("page") int page);

    @GET("movie/top_rated")
    Call<ServiceResult> callMostRated(@Query("api_key") String key, @Query("page") int page);

    @GET("/movie/{id}/videos")
    Call<ServiceResult> callVideos(@Query("api_key") String key, @Path("id") int id);

    @GET("/movie/{id}/reviews")
    Call<ServiceResult> callReviews(@Query("api_key") String key, @Path("id") int id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}

