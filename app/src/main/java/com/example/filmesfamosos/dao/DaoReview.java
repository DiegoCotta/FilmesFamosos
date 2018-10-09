package com.example.filmesfamosos.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.filmesfamosos.model.Review;

import java.util.List;

/**
 * Created by diegocotta on 09/10/2018.
 */

@Dao
public interface DaoReview {
    @Query("SELECT * FROM review WHERE idMovie = :idMovie ORDER BY id")
    LiveData<List<Review>> getReviews(int idMovie);

    @Insert
    void insertReview(Review review);

    @Delete
    void deleteReview(Review review);
}
