package com.example.filmesfamosos;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by diegocotta on 30/09/2018.
 */
//Reference https://zeroturnaround.com/rebellabs/getting-started-with-retrofit-2/
interface Service {

    @GET("/movie/popular")
    Call<ServiceResult> callMostPopular(@Query("api_key") String key, @Query("page") int page);

    @GET("/movie/top_rated")
    Call<ServiceResult> callMostRated(@Query("api_key") String key, @Query("page") int page);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
