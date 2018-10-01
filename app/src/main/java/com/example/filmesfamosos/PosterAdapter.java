package com.example.filmesfamosos;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by diegocotta on 28/09/2018.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private ArrayList<Movie> movies;
    private final PosterAdapterListener itemClickListener;
    private Context context;

    public PosterAdapter(PosterAdapterListener listener) {
        this.itemClickListener = listener;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_poster, parent, false);
        context = parent.getContext();
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(movies.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        else
            return movies.size();

    }

    public interface PosterAdapterListener {
        void onClick(Movie movie);
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final ImageView ivPoster;

        public PosterViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_Poster);

        }

        void bind(int position) {
            tvTitle.setText(movies.get(position).getTitle());
            Picasso.get().load(context.getString(R.string.base_image_url) + movies.get(position).getPosterPath()).into(ivPoster);
        }

    }
}
