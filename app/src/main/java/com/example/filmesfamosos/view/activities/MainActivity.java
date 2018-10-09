package com.example.filmesfamosos.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ActivityMainBinding;
import com.example.filmesfamosos.service.RequestType;
import com.example.filmesfamosos.service.Service;
import com.example.filmesfamosos.model.ServiceResult;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.utils.Util;
import com.example.filmesfamosos.view.adapter.PosterAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterListener {

    private PosterAdapter posterAdapter;
    private int numPages, maxPages;
    private RequestType lastRequest;
    private Service service;
    ActivityMainBinding binding;
    boolean refresh = false;
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.most_popular);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        binding.rvMoviesPoster.setLayoutManager(manager);
        binding.rvMoviesPoster.setHasFixedSize(true);
        posterAdapter = new PosterAdapter(MainActivity.this, binding.rvMoviesPoster);
        binding.rvMoviesPoster.setAdapter(posterAdapter);

        service = Service.retrofit.create(Service.class);
        numPages = 1;
        callMostPopular(numPages);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        switch (menuItemThatWasSelected) {
            case R.id.action_most_popular:
                setTitle(R.string.most_popular);
                if (lastRequest != RequestType.mostPopular)
                    numPages = 1;
                callMostPopular(numPages);
                break;
            case R.id.action_refresh:
                refresh = true;
                if (lastRequest == RequestType.mostPopular)
                    callMostPopular(numPages);
                else
                    callMostRated(numPages);
                break;
            case R.id.action_top_rating:
                setTitle(R.string.top_rating);
                if (lastRequest != RequestType.topRated)
                    numPages = 1;
                callMostRated(numPages);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callMostPopular(int page) {
        if (Util.hasInternet(this)) {
            if (numPages == 1 || refresh)
                showProgressBar();
            lastRequest = RequestType.mostPopular;
            service.callMostPopular(API_KEY, page).enqueue(new Callback<ServiceResult>() {
                @Override
                public void onResponse(Call<ServiceResult> call, Response<ServiceResult> response) {
                    responseSuccess(response);
                }

                @Override
                public void onFailure(Call<ServiceResult> call, Throwable t) {
                    responseError(call);
                }
            });
        } else {
            showConnectionError();
        }
    }

    private void callMostRated(int page) {
        if (Util.hasInternet(this)) {
            if (numPages == 1 || refresh)
                showProgressBar();
            lastRequest = RequestType.topRated;
            service.callMostRated(API_KEY, page).enqueue(new Callback<ServiceResult>() {
                @Override
                public void onResponse(Call<ServiceResult> call, Response<ServiceResult> response) {
                    responseSuccess(response);
                }

                @Override
                public void onFailure(Call<ServiceResult> call, Throwable t) {
                    responseError(call);
                }
            });
        } else {
            showConnectionError();

        }
    }

    private void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void showConnectionError() {
        refresh = false;
        posterAdapter.setLoaded();
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    private void responseSuccess(Response<ServiceResult> response) {
        if (response != null && response.body() != null) {
            maxPages = response.body().getTotal_pages();
            List<Movie> movieList = null;
            if (numPages > 1) {
                posterAdapter.getMovies().remove(posterAdapter.getMovies().size() - 1);
                movieList = posterAdapter.getMovies();
            }

            if (movieList == null || numPages == 1) {
                movieList = new ArrayList<>();
                if (!refresh)
                    binding.rvMoviesPoster.smoothScrollToPosition(0);
            }
            movieList.addAll(response.body().getMovies());
            posterAdapter.setMovies(movieList);
        }
        if (numPages == 1 || refresh) {
            refresh = false;
            hideProgressBar();
        }
    }

    private void responseError(Call<ServiceResult> call) {
        hideProgressBar();
        refresh = false;
        Toast.makeText(MainActivity.this, getString(R.string.service_error), Toast.LENGTH_SHORT).show();
        posterAdapter.setLoaded();
    }


    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_KEY, movie);
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        posterAdapter.getMovies().add(null);
        posterAdapter.notifyItemInserted(posterAdapter.getMovies().size() - 1);
        numPages++;
        if (numPages >= maxPages) {
            Toast.makeText(this, R.string.all_movies_load, Toast.LENGTH_SHORT).show();
        } else if (lastRequest == RequestType.mostPopular) {
            callMostPopular(numPages);
        } else if (lastRequest == RequestType.topRated) {
            callMostRated(numPages);
        }

    }


}
