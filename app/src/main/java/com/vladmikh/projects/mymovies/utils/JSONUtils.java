package com.vladmikh.projects.mymovies.utils;

import android.util.Log;

import com.vladmikh.projects.mymovies.data.Movie;
import com.vladmikh.projects.mymovies.data.Review;
import com.vladmikh.projects.mymovies.data.Trailer;

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

    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String LARGE_POSTER_SIZE = "w780";

    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        if (jsonObject == null) {
            return trailers;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTrailer = jsonArray.getJSONObject(i);
                String keyPath = BASE_YOUTUBE_URL + jsonTrailer.getString(KEY_OF_VIDEO);
                String name = jsonTrailer.getString(KEY_NAME);

                Trailer trailer = new Trailer(keyPath, name);
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Review> reviews = new ArrayList<>();
        if (jsonObject == null) {
            return reviews;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReview = jsonArray.getJSONObject(i);
                String author = jsonReview.getString(KEY_AUTHOR);
                String content = jsonReview.getString(KEY_CONTENT);
                Review review = new Review(author,content);
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

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
                String posterPath = POSTER_BASE_URL + SMALL_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH);
                String largePosterPath = POSTER_BASE_URL +LARGE_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH);
                String backdropPath = jsonMovie.getString(KEY_BACKDROP_PATH);
                double voteAverage = jsonMovie.getDouble(KEY_VOTE_AVERAGE);
                String releaseDate = jsonMovie.getString(KEY_REALISE_DATE);

                Movie movie = new Movie(id, voteCount,title, originalTitle, overview,posterPath,
                        largePosterPath, backdropPath, voteAverage, releaseDate);
                results.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}
