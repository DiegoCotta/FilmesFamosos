package com.example.filmesfamosos.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ActivityMovieDetailsBinding;
import com.example.filmesfamosos.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {


    public static final String base_image_url = BuildConfig.BASE_IMAGE_URL;
    boolean a = false;
    public static final String MOVIE_KEY = "movie_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            Movie movie = intent.getParcelableExtra(MOVIE_KEY);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

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
                if (a) {
                    item.setIcon(R.drawable.ic_heart_empty);
                } else {
                    item.setIcon(R.drawable.ic_heart_full);
                }
                a = !a;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
