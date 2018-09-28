package com.example.filmesfamosos;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diegocotta on 28/09/2018.
 */

class Movie {

    @SerializedName("id")
    int id;
    @SerializedName("voteAverage")
    float voteAverage;
    @SerializedName("title")
    String title;
    @SerializedName("popularity")
    int popularity;
    @SerializedName("posterPath")
    String posterPath;
    @SerializedName("imgPoster")
    Bitmap imgPoster;
    @SerializedName("overview")
    String overview;
    @SerializedName("release_date")
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

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
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
