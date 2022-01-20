package com.vladmikh.projects.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vladmikh.projects.mymovies.adapters.MovieAdapter;
import com.vladmikh.projects.mymovies.data.MainViewModel;
import com.vladmikh.projects.mymovies.data.Movie;
import com.vladmikh.projects.mymovies.utils.JSONUtils;
import com.vladmikh.projects.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    Switch switchSortBy;
    TextView textViewPopularity;
    TextView textViewTopRated;
    MainViewModel viewModel;

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
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        switchSortBy = findViewById(R.id.switchSortBy);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        chooseMoviesSortBy(false);
        getData();

        switchSortBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chooseMoviesSortBy(b);
            }
        });

        movieAdapter.setOnMovieClickListener(new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.MOVIE_ID, movieAdapter.getMovies().get(position).getId());
                startActivity(intent);
            }
        });

        movieAdapter.setOnReachEndMoviesListener(new MovieAdapter.OnReachEndMoviesListener() {
            @Override
            public void onReachEndMovies() {
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chooseMoviesSortBy(boolean isTopRated) {
        int sortBy;
        if (isTopRated) {
            sortBy = NetworkUtils.SORT_BY_VOTE_AVERAGE_VALUE_NUM;
            textViewTopRated.setTextColor(getResources().getColor(R.color.selected_element));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        } else {
            sortBy = NetworkUtils.SORT_BY_POPULARITY_VALUE_NUM;
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.selected_element));
        }
        downloadData(sortBy, 1);

    }

    public void onClickSortByPopularity(View view) {
        chooseMoviesSortBy(false);
        switchSortBy.setChecked(false);
    }

    public void onClickSortByTopRated(View view) {
        chooseMoviesSortBy(true);
        switchSortBy.setChecked(true);
    }

    private void getData() {
        LiveData<List<Movie>> moviesDB = viewModel.getMovies();
        moviesDB.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });
    }

    private void downloadData (int sortBy, int page) {
        JSONObject result = NetworkUtils.getJSONFromNetwork(sortBy, 1);
        ArrayList<Movie> movies = JSONUtils.getMovieFromJSON(result);
        if (movies != null && movies.size() > 0) {
            viewModel.deleteAllMovies();
            for (Movie m : movies) {
                viewModel.insertMovie(m);
            }
        }
    }
}