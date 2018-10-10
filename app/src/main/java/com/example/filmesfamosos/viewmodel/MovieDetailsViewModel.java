package com.example.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.dao.AppDatabase;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Review;
import com.example.filmesfamosos.model.ServiceResult;
import com.example.filmesfamosos.model.ServiceVideoResult;
import com.example.filmesfamosos.model.Video;
import com.example.filmesfamosos.service.Service;
import com.example.filmesfamosos.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diegocotta on 09/10/2018.
 */

public class MovieDetailsViewModel extends AndroidViewModel {

    private static final String API_KEY = BuildConfig.API_KEY;
    private final Service service;
    MutableLiveData<List<Video>> videos;
    MutableLiveData<List<Review>> reviews;
    LiveData<Movie> favoriteMovie;

    AppDatabase database;

    public MovieDetailsViewModel(@NonNull Application application) {

        super(application);
        favoriteMovie = new MutableLiveData<>();
        videos = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        service = Service.retrofit.create(Service.class);
        database = AppDatabase.getInstance(this.getApplication());

    }


    public void loadInfo(final int idMovie) {
        service.callVideos(idMovie, API_KEY).enqueue(new Callback<ServiceVideoResult>() {
            @Override
            public void onResponse(Call<ServiceVideoResult> call, Response<ServiceVideoResult> response) {
                if (response.body().getMovies() != null)
                    for (Video v : response.body().getMovies()) {
                        v.setIdMovie(idMovie);
                    }
                videos.setValue(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<ServiceVideoResult> call, Throwable t) {
                t.getMessage();
            }
        });

        service.callReviews(idMovie, API_KEY).enqueue(new Callback<ServiceResult<Review>>() {
            @Override
            public void onResponse(Call<ServiceResult<Review>> call, Response<ServiceResult<Review>> response) {
                if (response.body().getResults() != null) {
                    for (Review r : response.body().getResults()) {
                        r.setIdMovie(idMovie);

                    }
                }
                reviews.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<ServiceResult<Review>> call, Throwable t) {

            }
        });
        favoriteMovie = database.movieDao().getMovie(idMovie);

    }


    public void saveFavorite(final Movie movie) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    database.beginTransaction();
                    database.movieDao().insertMovie(movie);
                    database.reviewDao().insertReview(new ArrayList<>(reviews.getValue()));
                    database.videoDao().insertVideo(new ArrayList<>(videos.getValue()));
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }


            }
        });


    }

    public void removeFavorite(final Movie movie) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    database.beginTransaction();

                    database.movieDao().deleteMovie(movie);
                    database.videoDao().deleteVideos(movie.getId());
                    database.reviewDao().deleteReviews(movie.getId());
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
            }
        });

    }

    public LiveData<Movie> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setFavoriteMovie(MutableLiveData<Movie> favoriteMovie) {
        this.favoriteMovie = favoriteMovie;
    }

    public MutableLiveData<List<Video>> getVideos() {
        return videos;
    }

    public void setVideos(MutableLiveData<List<Video>> videos) {
        this.videos = videos;
    }

    public MutableLiveData<List<Review>> getReviews() {
        return reviews;
    }

    public void setReviews(MutableLiveData<List<Review>> reviews) {
        this.reviews = reviews;
    }
}