package com.example.filmesfamosos;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by diegocotta on 28/09/2018.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    ArrayList<Movie> movies;
    ListItemClickListener itemClickListener;

    public PosterAdapter(ArrayList<Movie> movies, ListItemClickListener itemClickListener) {
        this.movies = movies;
        this.itemClickListener = itemClickListener;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_poster, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        else
            return movies.size();

    }

    public interface ListItemClickListener {
        void onClick(Movie movie);
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivPoster;

        public PosterViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_Poster);

        }

        void bind(int position) {

        }

    }
}
