package com.example.filmesfamosos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "movie_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ImageView ivPoster = (ImageView) findViewById(R.id.iv_Poster);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        TextView tvResume = (TextView) findViewById(R.id.tv_resume);
        TextView tvRating = (TextView) findViewById(R.id.tv_rating);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MOVIE_KEY)) {
            Movie movie = (Movie) intent.getSerializableExtra(MOVIE_KEY);
            Picasso.get().load(getString(R.string.base_image_url) + movie.getPosterPath()).into(ivPoster);
            tvTitle.setText(movie.getTitle());

            tvResume.setText(movie.getOverview());
            tvRating.setText(String.valueOf(movie.getVoteAverage()));

            //reference https://stackoverflow.com/questions/39445002/get-date-month-and-year-from-particular-date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(movie.getRelease_date());

                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                tvReleaseDate.setText(String.format("%d", cal.get(Calendar.YEAR)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
