package com.example.filmesfamosos.service;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Review;
import com.example.filmesfamosos.model.ServiceResult;
import com.example.filmesfamosos.model.ServiceVideoResult;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
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
    Call<ServiceResult<Movie>> callMostPopular(@Query("api_key") String key, @Query("page") int page);

    @GET("movie/top_rated")
    Call<ServiceResult<Movie>> callMostRated(@Query("api_key") String key, @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<ServiceVideoResult> callVideos(@Path("id") int id, @Query("api_key") String key);

    @GET("movie/{id}/reviews")
    Call<ServiceResult<Review>> callReviews(@Path("id") int id, @Query("api_key") String key);

    OkHttpClient okHttp = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build();


}

