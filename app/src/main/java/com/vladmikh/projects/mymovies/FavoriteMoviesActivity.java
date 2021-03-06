package com.vladmikh.projects.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vladmikh.projects.mymovies.adapters.MovieAdapter;
import com.vladmikh.projects.mymovies.data.FavouriteMovie;
import com.vladmikh.projects.mymovies.data.MainViewModel;
import com.vladmikh.projects.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MainViewModel viewModel;

    private static final int COUNT_DP_IN_ONE_INCH = 160;
    private static final int COUNT_MOVIE_BY_SCREEN_WIDTH = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentFavourite = new Intent(this, FavoriteMoviesActivity.class);
                startActivity(intentFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        recyclerView = findViewById(R.id.recyclerViewFavouriteMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        movieAdapter = new MovieAdapter();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<FavouriteMovie>> favouriteMovies = viewModel.getFavouriteMovies();
        recyclerView.setAdapter(movieAdapter);

        favouriteMovies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {
                List<Movie> movies = new ArrayList<>();
                if (favouriteMovies != null) {
                    movies.addAll(favouriteMovies);
                    movieAdapter.setMovies(movies);
                }
            }
        });

        movieAdapter.setOnMovieClickListener(new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(int position) {
                Intent intent = new Intent(FavoriteMoviesActivity.this, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.MOVIE_ID, movieAdapter.getMovies().get(position).getId());
                startActivity(intent);
            }
        });
    }

    private int getColumnCount() {
        int result = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        if (width / COUNT_DP_IN_ONE_INCH < COUNT_MOVIE_BY_SCREEN_WIDTH ){
            result = COUNT_MOVIE_BY_SCREEN_WIDTH;
        } else {
            result = width / COUNT_DP_IN_ONE_INCH;
        }
        return result;
    }
}