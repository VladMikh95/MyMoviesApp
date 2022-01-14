package com.vladmikh.projects.mymovies.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase database;
    private static final String DATABASE_NAME = "movies.db";
    private static final Object LOCK = new Object();

    public static MoviesDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MoviesDatabase.class, DATABASE_NAME).build();
            }
        }
        return database;
    }

    public abstract MovieDao movieDao();
}
