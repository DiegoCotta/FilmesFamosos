package com.example.filmesfamosos.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.filmesfamosos.model.Movie;

import java.util.List;

/**
 * Created by diegocotta on 08/10/2018.
 */

@Dao
public interface DaoMovie {

    @Query("SELECT * FROM movie ORDER BY id")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movie WHERE id =:id")
    LiveData<Movie> getMovie(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

}
