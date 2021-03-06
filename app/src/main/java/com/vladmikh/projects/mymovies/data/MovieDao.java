package com.vladmikh.projects.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE ID ==:movieId")
    Movie getMovie(int movieId);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Delete
    void deleteMovie(Movie movie);

    @Insert
    void insertMovie(Movie movie);

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovie();

    @Query("SELECT * FROM favourite_movies WHERE ID ==:movieId")
    FavouriteMovie getFavouriteMovie(int movieId);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie);

    @Insert
    void insertFavouriteMovie(FavouriteMovie movie);

}
