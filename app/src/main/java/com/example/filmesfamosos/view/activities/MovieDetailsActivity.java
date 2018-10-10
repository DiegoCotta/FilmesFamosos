package com.example.filmesfamosos.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ActivityMovieDetailsBinding;
import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Review;
import com.example.filmesfamosos.model.Video;
import com.example.filmesfamosos.utils.Util;
import com.example.filmesfamosos.view.adapter.ReviewAdapter;
import com.example.filmesfamosos.view.adapter.VideoAdapter;
import com.example.filmesfamosos.viewmodel.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsViewModel.MovieDetailsListener, VideoAdapter.VideoListener {


    public static final String base_image_url = BuildConfig.BASE_IMAGE_URL;
    public static final String MOVIE_KEY = "movie_key";
    private MovieDetailsViewModel viewModel;
    private Movie movie;
    private Menu mOptionsMenu;
    private ActivityMovieDetailsBinding binding;
    private ReviewAdapter reviewAdapter;
    private VideoAdapter videoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            movie = intent.getParcelableExtra(MOVIE_KEY);
            setTitle(movie.getTitle());
            Picasso.get().load(base_image_url + movie.getPosterPath()).into(binding.ivPoster);

            binding.tvResume.setText(movie.getOverview());
            binding.tvRating.setText(String.valueOf(movie.getVoteAverage()));

            //reference https://stackoverflow.com/questions/39445002/get-date-month-and-year-from-particular-date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(movie.getRelease_date());

                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                binding.tvReleaseDate.setText(String.format("%d", cal.get(Calendar.YEAR)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        LinearLayoutManager layoutManagerHorizontal
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerVertical
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvReview.setLayoutManager(layoutManagerVertical);
        binding.rvReview.setHasFixedSize(true);
        binding.rvVideos.setLayoutManager(layoutManagerHorizontal);
        binding.rvVideos.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter();
        videoAdapter = new VideoAdapter(this);
        binding.rvReview.setAdapter(reviewAdapter);
        binding.rvVideos.setAdapter(videoAdapter);
        binding.progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupViewModel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Util.hasInternet(this)) {
            showProgressBar();
            viewModel.getDatabase().videoDao().getVideos(movie.getId()).observe(this, new Observer<List<Video>>() {
                @Override
                public void onChanged(@Nullable List<Video> videos) {
                    videoAdapter.setVideos(videos);
                    if (reviewAdapter.getItemCount() > 0) {
                        hideProgressBar();
                    }
                }
            });
            viewModel.getDatabase().reviewDao().getReviews(movie.getId()).observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(@Nullable List<Review> reviews) {
                    reviewAdapter.setReviews(reviews);
                    if (videoAdapter.getItemCount() > 0)
                        hideProgressBar();
                }
            });
        } else {
            viewModel.loadInfo(movie.getId());
        }
        viewModel.isFavorite(movie.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        mOptionsMenu = menu;
        viewModel.getFavoriteMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null)
                    mOptionsMenu.getItem(0).setIcon(R.drawable.ic_heart_full);
                else
                    mOptionsMenu.getItem(0).setIcon(R.drawable.ic_heart_empty);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        switch (menuItemThatWasSelected) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_favorite:
                if (viewModel.getFavoriteMovie().getValue() != null) {
                    viewModel.removeFavorite(movie);
                } else {
                    viewModel.saveFavorite(movie);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        viewModel.setListener(this);
        viewModel.getVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                videoAdapter.setVideos(videos);
            }
        });
        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });


    }


    @Override
    public void showError(int message) {
        hideProgressBar();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onVideoClick(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        PackageManager packageManager = getPackageManager();
        if (appIntent.resolveActivity(packageManager) != null)
            startActivity(appIntent);
        else
            startActivity(webIntent);
    }

}
