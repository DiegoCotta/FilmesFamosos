package com.example.filmesfamosos.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ActivityMainBinding;
import com.example.filmesfamosos.service.RequestType;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.utils.Util;
import com.example.filmesfamosos.view.adapter.PosterAdapter;
import com.example.filmesfamosos.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterListener, MainViewModel.MainListener {

    private PosterAdapter posterAdapter;

    MainViewModel viewModel;

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        GridLayoutManager manager = new GridLayoutManager(this, getResources().getInteger(R.integer.gridSize));

        binding.rvMoviesPoster.setLayoutManager(manager);
        binding.rvMoviesPoster.setHasFixedSize(true);
        posterAdapter = new PosterAdapter(MainActivity.this, binding.rvMoviesPoster, this);
        binding.rvMoviesPoster.setAdapter(posterAdapter);
        setupViewModel();
        viewModel.setListener(this);
        if (Util.hasInternet(this)) {
            setTitle(R.string.most_popular);
            viewModel.loadMovies(RequestType.mostPopular);
        } else {
            setTitle(R.string.favorites);
            viewModel.loadMovies(RequestType.favorite);
        }
        binding.progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
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
                viewModel.loadMovies(RequestType.mostPopular);
                break;
            case R.id.action_refresh:
                viewModel.loadMovies(null);
                break;
            case R.id.action_top_rating:
                viewModel.loadMovies(RequestType.topRated);
                break;
            case R.id.favorites:
                viewModel.loadMovies(RequestType.favorite);
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
    public void setScreenTitle(int title) {
        setTitle(title);
    }

    @Override
    public void showError(int messageId) {
        posterAdapter.setLoaded();
        hideProgressBar();
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
