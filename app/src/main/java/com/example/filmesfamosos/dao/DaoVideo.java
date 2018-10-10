package com.example.filmesfamosos.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.filmesfamosos.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diegocotta on 09/10/2018.
 */

@Dao
public interface DaoVideo {

    @Query("SELECT * FROM video WHERE idMovie = :idMovie ORDER BY idVideo")
    LiveData<List<Video>> getVideos(int idMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(ArrayList<Video> video);

    @Query("DELETE FROM video WHERE idMovie = :idMovie")
    void deleteVideos(int idMovie);

}
