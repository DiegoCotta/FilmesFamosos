package com.example.filmesfamosos.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.filmesfamosos.model.Movie;
import com.example.filmesfamosos.model.Review;
import com.example.filmesfamosos.model.Video;

/**
 * Created by diegocotta on 09/10/2018.
 */

@Database(entities = {Movie.class, Review.class, Video.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "filmes-famosos";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DaoVideo videoDao();

    public abstract DaoReview reviewDao();

    public abstract DaoMovie movieDao();

}


