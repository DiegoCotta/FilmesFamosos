package com.example.filmesfamosos.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Video;

import java.util.List;

/**
 * Created by diegocotta on 09/10/2018.
 */

@Dao
public interface DaoVideo {

    @Query("SELECT * FROM video WHERE idMovie = :idMovie ORDER BY id")
    LiveData<List<Video>> getMovies(int idMovie);

    @Insert
    void insertMovie(Movie review);

    @Delete
    void deleteMovie(Movie review);
}
