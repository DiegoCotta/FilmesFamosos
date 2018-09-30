package com.example.filmesfamosos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterListener {

    private RecyclerView rvMoviesPoster;
    private PosterAdapter posterAdapter;
    private ProgressBar progressBar;
    private int numPages;
    private Request lastRequest;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMoviesPoster = (RecyclerView) findViewById(R.id.rv_movies_poster);
        posterAdapter = new PosterAdapter(MainActivity.this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvMoviesPoster.setLayoutManager(manager);
        rvMoviesPoster.setHasFixedSize(true);
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
                if (lastRequest == Request.mostPopular)
                    numPages++;
                else
                    numPages = 1;
                callMostPopular(numPages);
                break;
            case R.id.action_refresh:
                if (lastRequest == Request.mostPopular)
                    callMostPopular(numPages);
                else
                    callMostRated(numPages);
                break;
            case R.id.action_top_rating:
                if (lastRequest == Request.topRated)
                    numPages++;
                else
                    numPages = 1;
                callMostRated(numPages);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callMostPopular(int page) {
        showProgressBar();
        lastRequest = Request.mostPopular;
        service.callMostPopular(getString(R.string.api_key), page).enqueue(new Callback<ServiceResult>() {
            @Override
            public void onResponse(Call<ServiceResult> call, Response<ServiceResult> response) {
                hideProgressBar();
                if(response != null && response.body() != null)
                posterAdapter.setMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<ServiceResult> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(MainActivity.this, getString(R.string.service_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callMostRated(int page) {
        showProgressBar();
        lastRequest = Request.topRated;
        service.callMostRated(getString(R.string.api_key), page).enqueue(new Callback<ServiceResult>() {
            @Override
            public void onResponse(Call<ServiceResult> call, Response<ServiceResult> response) {
                hideProgressBar();
                if(response != null && response.body() != null)
                posterAdapter.setMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<ServiceResult> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(MainActivity.this, getString(R.string.service_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
}

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_KEY, movie);
        startActivity(intent);
    }

    @Override
    public void loadMore() {
        if (lastRequest == Request.mostPopular)
            callMostPopular(++numPages);
        else
            callMostRated(++numPages);
    }
}
