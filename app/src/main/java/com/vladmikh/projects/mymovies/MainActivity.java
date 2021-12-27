package com.vladmikh.projects.mymovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.vladmikh.projects.mymovies.data.Movie;
import com.vladmikh.projects.mymovies.utils.JSONUtils;
import com.vladmikh.projects.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONObject result = NetworkUtils.getJSONFromNetwork(1, 1);
        ArrayList<Movie> movies = JSONUtils.getMovieFromJSON(result);
        StringBuilder builder = new StringBuilder();
        for (Movie i : movies) {
            builder.append(i.getTitle()).append("\n");
        }
        Log.i("abc", builder.toString());
    }
}