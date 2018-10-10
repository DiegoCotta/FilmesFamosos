package com.example.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.dao.AppDatabase;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.ServiceResult;
import com.example.filmesfamosos.service.RequestType;
import com.example.filmesfamosos.service.Service;
import com.example.filmesfamosos.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diegocotta on 09/10/2018.
 */

public class MainViewModel extends AndroidViewModel implements LifecycleOwner {

    private final Service service;
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<List<Movie>> movies;
    private int numPages = 1, maxPages;
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private RequestType lastRequest;
    private MainListener listener;

    public MainViewModel(Application application) {
        super(application);
        startListening();
        movies = new MutableLiveData<>();
        service = Service.retrofit.create(Service.class);
    }


    public void loadMovies(RequestType type) {
        boolean refresh = false;
        if (type == null) {
            type = lastRequest;
            refresh = true;
            numPages--;
        }
        switch (type) {
            case mostPopular:
                if (lastRequest == RequestType.mostPopular)
                    numPages++;
                else
                    numPages = 1;
                callMostPopular(numPages, refresh);
                break;
            case topRated:
                if (lastRequest == RequestType.topRated)
                    numPages++;
                else
                    numPages = 1;
                callMostRated(numPages, refresh);
                break;
            case favorite:
                callFavorite();
                break;
        }

    }

    public void loadMoreMovies() {
        if (numPages >= maxPages) {
            listener.showError(R.string.all_movies_load);
        } else
            loadMovies(lastRequest);
    }


    private void callFavorite() {
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        lastRequest = RequestType.favorite;
        listener.showProgressBar();
        database.movieDao().getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                movies.setValue(movieList);
                listener.setScreenTitle(R.string.favorites);
                listener.hideProgressBar();
            }
        });

    }


    private void callMostPopular(int page, final boolean refresh) {
        if (Util.hasInternet(getApplication())) {
            if (page == 1 || refresh)
                listener.showProgressBar();
            lastRequest = RequestType.mostPopular;
            service.callMostPopular(API_KEY, page).enqueue(new Callback<ServiceResult<Movie>>() {
                @Override
                public void onResponse(Call<ServiceResult<Movie>> call, Response<ServiceResult<Movie>> response) {
                    if (response.body() != null && response.body().getResults()  != null && lastRequest == RequestType.mostPopular) {
                        maxPages = response.body().getTotal_pages();
                        List<Movie> movieList = null;
                        if (numPages > 1) {
                            movieList = movies.getValue();
                            movieList.remove(movieList.size() - 1);
                        }

                        if (movieList == null || numPages == 1) {
                            movieList = new ArrayList<>();
                            if (!refresh)
                                listener.moveToTop();
                        }
                        movieList.addAll(response.body().getResults());
                        movies.setValue(movieList);
                    }

                    if (numPages == 1 || refresh) {
                        listener.hideProgressBar();
                    }
                    listener.setScreenTitle(R.string.top_rating);
                }

                @Override
                public void onFailure(Call<ServiceResult<Movie>> call, Throwable t) {
                    listener.showError(R.string.service_error);
                }
            });
        } else {
            listener.showError(R.string.no_internet);
        }
    }

    private void callMostRated(int page, final boolean refresh) {
        if (Util.hasInternet(getApplication())) {
            if (page == 1 || refresh)
                listener.showProgressBar();
            lastRequest = RequestType.topRated;
            service.callMostRated(API_KEY, page).enqueue(new Callback<ServiceResult<Movie>>() {
                @Override
                public void onResponse(Call<ServiceResult<Movie>> call, Response<ServiceResult<Movie>> response) {
                    if (response.body() != null && response.body().getResults()  != null && lastRequest == RequestType.topRated) {
                        maxPages = response.body().getTotal_pages();
                        List<Movie> movieList = null;
                        if (numPages > 1) {
                            movieList = movies.getValue();
                            movieList.remove(movieList.size() - 1);
                        }

                        if (movieList == null || numPages == 1) {
                            movieList = new ArrayList<>();
                            if (!refresh)
                                listener.moveToTop();
                            // binding.rvMoviesPoster.smoothScrollToPosition(0);
                        }
                        movieList.addAll(response.body().getResults());
                        movies.setValue(movieList);
                    }

                    if (numPages == 1 || refresh) {
                        listener.hideProgressBar();
                    }
                    listener.setScreenTitle(R.string.most_popular);
                }

                @Override
                public void onFailure(Call<ServiceResult<Movie>> call, Throwable t) {
                    listener.showError(R.string.service_error);
                }
            });
        } else {
            listener.showError(R.string.no_internet);

        }
    }


    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(MutableLiveData<List<Movie>> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    public interface MainListener {
        void showError(int message);

        void showProgressBar();

        void hideProgressBar();

        void moveToTop();

        void setScreenTitle(int title);
    }

    private void startListening() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    public MainListener getListener() {
        return listener;
    }

    public void setListener(MainListener listener) {
        this.listener = listener;
    }
}
