package com.example.filmesfamosos.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filmesfamosos.R;
import com.example.filmesfamosos.databinding.ItemVideoBinding;
import com.example.filmesfamosos.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diegocotta on 10/10/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videos;
    private final VideoListener listener;
    private static final String base_image_url = "https://img.youtube.com/vi/%s/0.jpg";

    public VideoAdapter(VideoListener listener) {
        this.listener = listener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVideoBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video, parent, false);
        return new VideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        holder.bind(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onVideoClick(videos.get(holder.getAdapterPosition()).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videos == null)
            return 0;
        else
            return videos.size();
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        final ItemVideoBinding binding;

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {

            binding.tvName.setText(videos.get(position).getName());
            Picasso.get().load(String.format(base_image_url, videos.get(position).getKey())).into(binding.ivVideo);
        }
    }

    public interface VideoListener {
        void onVideoClick(String key);
    }
}