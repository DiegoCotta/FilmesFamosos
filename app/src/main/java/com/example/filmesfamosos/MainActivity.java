package com.example.filmesfamosos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PosterAdapter.ListItemClickListener {

    RecyclerView rvMoviesPoster;
    PosterAdapter posterAdapter;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMoviesPoster = (RecyclerView) findViewById(R.id.rv_movies_poster);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvMoviesPoster.setLayoutManager(manager);
        rvMoviesPoster.setHasFixedSize(true);

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
                break;
            case R.id.action_refresh:
                break;
            case R.id.action_top_rating:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {

    }
}
