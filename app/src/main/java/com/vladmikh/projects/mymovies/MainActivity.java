package com.vladmikh.projects.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vladmikh.projects.mymovies.adapters.MovieAdapter;
import com.vladmikh.projects.mymovies.data.MainViewModel;
import com.vladmikh.projects.mymovies.data.Movie;
import com.vladmikh.projects.mymovies.utils.JSONUtils;
import com.vladmikh.projects.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private Switch switchSortBy;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private MainViewModel viewModel;
    private ProgressBar progressBarLoading;

    private static final int LOADER_ID = 111;
    private LoaderManager loaderManager;

    private static int page = 1;
    private static int sortBy;
    private static boolean isLoading =false;

    private static final int COUNT_DP_IN_ONE_INCH = 160;
    private static final int COUNT_MOVIE_BY_SCREEN_WIDTH = 2;

    private String language;

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
        language = Locale.getDefault().getLanguage();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        loaderManager =LoaderManager.getInstance(this);
        switchSortBy = findViewById(R.id.switchSortBy);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        chooseMoviesSortBy(false);
        getData();

        switchSortBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page = 1;
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
                if (!isLoading) {
                    downloadData(sortBy,page);
                }
            }
        });
    }


    private void chooseMoviesSortBy(boolean isTopRated) {
        if (isTopRated) {
            sortBy = NetworkUtils.SORT_BY_VOTE_AVERAGE_VALUE_NUM;
            textViewTopRated.setTextColor(getResources().getColor(R.color.selected_element));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        } else {
            sortBy = NetworkUtils.SORT_BY_POPULARITY_VALUE_NUM;
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.selected_element));
        }
        downloadData(sortBy, page);

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
                if (page == 1) {
                    movieAdapter.setMovies(movies);
                }
            }
        });
    }

    private int getColumnCount() {
        int result = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        if (width / COUNT_DP_IN_ONE_INCH <= COUNT_MOVIE_BY_SCREEN_WIDTH) {
            result = COUNT_MOVIE_BY_SCREEN_WIDTH;
        } else {
            result = width / COUNT_DP_IN_ONE_INCH;
        }
        return result;
    }

    private void downloadData (int sortBy, int page) {
        URL url = NetworkUtils.buildURL(sortBy, page, language);
        Bundle bundle = new Bundle();
        bundle.putString(NetworkUtils.URL_KEY, url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMovieFromJSON(data);
        if (movies != null && movies.size() > 0) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for (Movie m : movies) {
                viewModel.insertMovie(m);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.GONE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}