package com.example.filmesfamosos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by diegocotta on 30/09/2018.
 */

public class ServiceResult {
    @SerializedName("page")
    private
    int page;
    @SerializedName("total_results")
    private
    int total_results;
    @SerializedName("total_pages")
    private
    int total_pages;

    @SerializedName("results")
    private
    ArrayList<Movie> movies;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
