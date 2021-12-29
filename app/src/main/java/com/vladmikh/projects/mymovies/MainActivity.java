package com.vladmikh.projects.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.vladmikh.projects.mymovies.data.Movie;
import com.vladmikh.projects.mymovies.utils.JSONUtils;
import com.vladmikh.projects.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        RecyclerView recyclerView = null;
        MovieAdapter movieAdapter = null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        JSONObject result = NetworkUtils.getJSONFromNetwork(1, 1);
        ArrayList<Movie> movies = JSONUtils.getMovieFromJSON(result);
        movieAdapter.setMovies(movies);
        recyclerView.setAdapter(movieAdapter);

    }
}