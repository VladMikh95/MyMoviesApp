package com.vladmikh.projects.mymovies.utils;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL ="https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_TRAILER ="https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS ="https://api.themoviedb.org/3/movie/%s/reviews";

    private static final String PARAMS_API_KEY ="api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_VOTE_COUNT = "vote_count.gte";

    private static final String API_KEY_VALUE ="188ae58f236f44bf416d37ac632058c3";
    private static final String LANGUAGE_VALUE = "language";
    private static final String SORT_BY_POPULARITY_VALUE = "popularity.desc";
    private static final String SORT_BY_VOTE_AVERAGE_VALUE = "vote_average.desc";
    private static final int VOTE_COUNT = 1000;

    public static final int SORT_BY_POPULARITY_VALUE_NUM  = 0;
    public static final int SORT_BY_VOTE_AVERAGE_VALUE_NUM = 1;

    /*
    The method returns a parameter by which movies will be sorted depending on the user's choice
     */
    private static String getSortByValue (int sortBy) {
        String result;
        if (sortBy == SORT_BY_POPULARITY_VALUE_NUM) {
            result = SORT_BY_POPULARITY_VALUE;
        } else {
            result = SORT_BY_VOTE_AVERAGE_VALUE;
        }
        return result;
    }

    /*
    The method creates a Url to get data about trailers from the Internet
     */
    private static URL buildURLTrailers (int movieId) {
        URL result = null;
        String urlTrailer = String.format(BASE_URL_TRAILER, movieId);
        Uri uri = Uri.parse(urlTrailer).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    The method creates a Url to get data about reviews from the Internet
     */
    private static URL buildURLReviews (int movieId) {
        URL result = null;
        String urlReviews = String.format(BASE_URL_REVIEWS, movieId);
        Uri uri = Uri.parse(urlReviews).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /*
    The method creates a Url to get data from the Internet
     */
    private static URL buildURL(int sortBy, int page) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY_VALUE)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, getSortByValue(sortBy))
                .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                .appendQueryParameter(PARAMS_VOTE_COUNT, String.valueOf(VOTE_COUNT))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObjectForTrailers(int id) {
        JSONObject result = null;
        URL url = buildURLTrailers(id);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static JSONObject getJSONObjectForReviews(int id) {
        JSONObject result = null;
        URL url = buildURLReviews(id);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    The method returns data received from the Network
     */
    public static JSONObject getJSONFromNetwork (int sortBy, int page) {
        JSONObject result = null;
        URL url = buildURL(sortBy, page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;

            if (urls == null || urls.length == 0) {
                return result;
            }
            HttpURLConnection urlConection = null;
            try {
                urlConection = (HttpURLConnection) urls[0].openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while(line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConection != null) {
                    urlConection.disconnect();
                }
            }
            return result;
        }
    }


}
