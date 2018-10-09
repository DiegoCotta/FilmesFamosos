package com.example.filmesfamosos.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
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
import com.example.filmesfamosos.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterListener, MainViewModel.MainListener {

    private PosterAdapter posterAdapter;

    MainViewModel viewModel;

    ActivityMainBinding binding;
    boolean refresh = false;


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
        setupViewModel();
        viewModel.setListener(this);
        viewModel.loadMovies(RequestType.mostPopular);

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
                viewModel.loadMovies(RequestType.mostPopular);
                break;
            case R.id.action_refresh:
                refresh = true;
                viewModel.loadMovies(null);
                break;
            case R.id.action_top_rating:
                setTitle(R.string.top_rating);
                viewModel.loadMovies(RequestType.topRated);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void moveToTop() {
        binding.rvMoviesPoster.scrollToPosition(0);

    }

    @Override
    public void showError(int messageId) {
        refresh = false;
        posterAdapter.setLoaded();
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
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
        viewModel.loadMoreMovies();

    }


    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                posterAdapter.setMovies(movieList);
            }
        });
    }


}
