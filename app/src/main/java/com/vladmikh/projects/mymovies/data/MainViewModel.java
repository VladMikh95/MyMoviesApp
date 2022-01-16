package com.vladmikh.projects.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MoviesDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.movieDao().getAllMovies();
        favouriteMovies = database.movieDao().getAllFavouriteMovie();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public Movie getMovie(int movieId) {
        Movie movie = null;
        try {
            movie = new GetMovieTask().execute(movieId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public void deleteAllMovies() {
        new DeleteAllMoviewTask().execute();
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask().execute(movie);
    }

    public void insertMovie(Movie movie) {
        new InsertMovieTask().execute(movie);
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public FavouriteMovie getFavouriteMovie(int movieId) {
        FavouriteMovie favouriteMovie = null;
        try {
            favouriteMovie = new GetFavouriteMovieTask().execute(movieId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return favouriteMovie;
    }

    public void deleteFavouriteMovie(FavouriteMovie favouriteMovie) {
        new DeleteFavouriteMovieTask().execute(favouriteMovie);
    }

    public void insertFavouriteMovie (FavouriteMovie favouriteMovie) {
        new InsertFavouriteMovieTask().execute(favouriteMovie);
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            Movie movie = null;
            if (integers != null && integers.length > 0) {
                movie = database.movieDao().getMovie(integers[0]);
            }
            return movie;
        }
    }

    private static class DeleteAllMoviewTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.movieDao().deleteAllMovies();
            return null;
        }
    }

    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                database.movieDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {


        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                database.movieDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class GetFavouriteMovieTask extends AsyncTask<Integer, Void, FavouriteMovie> {

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            FavouriteMovie favouriteMovie = null;
            if (integers != null && integers.length > 0) {
                favouriteMovie = database.movieDao().getFavouriteMovie(integers[0]);
            }
            return favouriteMovie;
        }
    }

    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies != null && favouriteMovies.length > 0) {
                database.movieDao().deleteFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies != null && favouriteMovies.length > 0) {
                database.movieDao().insertFavouriteMovie(favouriteMovies[0]);
            }
            return null;
        }
    }
}
