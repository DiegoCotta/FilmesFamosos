package com.example.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.dao.AppDatabase;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Review;
import com.example.filmesfamosos.model.ServiceResult;
import com.example.filmesfamosos.model.ServiceVideoResult;
import com.example.filmesfamosos.model.Video;
import com.example.filmesfamosos.service.Service;
import com.example.filmesfamosos.utils.AppExecutors;

import java.util.ArrayList;
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
    private MutableLiveData<List<Video>> videos;
    private MutableLiveData<List<Review>> reviews;
    private LiveData<Movie> favoriteMovie;
    private MovieDetailsListener listener;

    private final AppDatabase database;

    public MovieDetailsViewModel(@NonNull Application application) {

        super(application);
        favoriteMovie = new MutableLiveData<>();
        videos = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        service = Service.retrofit.create(Service.class);
        database = AppDatabase.getInstance(this.getApplication());


    }

    public AppDatabase getDatabase() {
        return database;
    }


    public void loadInfo(final int idMovie) {
        listener.showProgressBar();
        service.callVideos(idMovie, API_KEY).enqueue(new Callback<ServiceVideoResult>() {
            @Override
            public void onResponse(Call<ServiceVideoResult> call, Response<ServiceVideoResult> response) {
                if (response.body().getMovies() != null)
                    for (int i = 0; i < response.body().getMovies().size(); i++) {
                        if (response.body().getMovies().get(i).getSite().toLowerCase().contains("youtube"))
                            response.body().getMovies().get(i).setIdMovie(idMovie);
                        else {
                            response.body().getMovies().remove(i--);
                        }
                    }
                videos.setValue(response.body().getMovies());
                if (reviews.getValue() != null) {
                    listener.hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<ServiceVideoResult> call, Throwable t) {
                listener.showError(R.string.service_error);
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
                if (videos.getValue() != null) {
                    listener.hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<ServiceResult<Review>> call, Throwable t) {
                listener.showError(R.string.service_error);
            }
        });

    }

    public void isFavorite(int idMovie){
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

    public MovieDetailsListener getListener() {
        return listener;
    }

    public void setListener(MovieDetailsListener listener) {
        this.listener = listener;
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

    public interface MovieDetailsListener {
        void showError(int message);

        void showProgressBar();

        void hideProgressBar();

    }
}
