package com.example.filmesfamosos.view.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmesfamosos.BuildConfig;
import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ItemLoadingBinding;
import com.example.filmesfamosos.databinding.ItemMoviePosterBinding;
import com.example.filmesfamosos.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diegocotta on 28/09/2018.
 */
//Reference Load More https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
public class PosterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String base_image_url = BuildConfig.BASE_IMAGE_URL;

    private final int VIEW_ITEM = 1;


    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    private List<Movie> movies;
    private final PosterAdapterListener adapterListener;
    private LayoutInflater layoutInflater;
    private final Context context;

    public PosterAdapter(PosterAdapterListener listener, RecyclerView recyclerView, Context context) {
        this.adapterListener = listener;
        this.context = context;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = gridLayoutManager.getItemCount();
                            lastVisibleItem = gridLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (adapterListener != null && movies != null && movies.size() > 5) {
                                    adapterListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == VIEW_ITEM)
                        return 1;
                    else
                        return PosterAdapter.this.context.getResources().getInteger(R.integer.gridSize);
                }
            });
        }

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        setLoaded();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == VIEW_ITEM) {

            ItemMoviePosterBinding binding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.item_movie_poster, parent, false);
            return new PosterViewHolder(binding);
        } else {
            ItemLoadingBinding binding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.item_loading, parent, false);
            return new ProgressViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return getMovies().get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PosterViewHolder) {

            ((PosterViewHolder) holder).bind(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onClick(movies.get(holder.getAdapterPosition()));
                }
            });
        } else {
            ((ProgressViewHolder) holder).binding.progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
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

        void onLoadMore();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {

        final ItemMoviePosterBinding binding;


        public PosterViewHolder(ItemMoviePosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void bind(int position) {
            binding.tvTitle.setText(movies.get(position).getTitle());
            Picasso.get().load(base_image_url + movies.get(position).getPosterPath()).into(binding.ivPoster);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        final ItemLoadingBinding binding;

        public ProgressViewHolder(ItemLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
