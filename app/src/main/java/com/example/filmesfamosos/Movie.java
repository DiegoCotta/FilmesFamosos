package com.example.filmesfamosos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by diegocotta on 28/09/2018.
 */

class Movie implements Serializable {

    @SerializedName("id")
    private
    int id;
    @SerializedName("vote_average")
    private
    float voteAverage;
    @SerializedName("title")
    private
    String title;
    @SerializedName("popularity")
    private
    String popularity;
    @SerializedName("poster_path")
    private
    String posterPath;
    @SerializedName("imgPoster")
    private
    Bitmap imgPoster;
    @SerializedName("overview")
    private
    String overview;
    @SerializedName("release_date")
    private
    String release_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Bitmap getImgPoster() {
        return imgPoster;
    }

    public void setImgPoster(Bitmap imgPoster) {
        this.imgPoster = imgPoster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
