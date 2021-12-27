package com.vladmikh.projects.mymovies.utils;

import com.vladmikh.projects.mymovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_REALISE_DATE = "release_date";

    public static ArrayList<Movie> getMovieFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> results = new ArrayList<>();
        if (jsonObject == null) {
            return results;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMovie = jsonArray.getJSONObject(i);
                int id = jsonMovie.getInt(KEY_ID);
                int voteCount = jsonMovie.getInt(KEY_VOTE_COUNT);
                String title = jsonMovie.getString(KEY_TITLE);
                String originalTitle = jsonMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = jsonMovie.getString(KEY_OVERVIEW);
                String posterPath = jsonMovie.getString(KEY_POSTER_PATH);
                String backdropPath = jsonMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage = jsonMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = jsonMovie.getString(KEY_REALISE_DATE);

                Movie movie = new Movie(id, voteCount,title, originalTitle, overview,posterPath,
                        backdropPath, voteAverage, releaseDate);
                results.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}
