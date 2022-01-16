package com.vladmikh.projects.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vladmikh.projects.mymovies.data.FavouriteMovie;
import com.vladmikh.projects.mymovies.data.MainViewModel;
import com.vladmikh.projects.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "id";

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ImageView imageViewAddDeleteFavouriteMovie;

    private Movie movie;
    private MainViewModel viewModel;
    private int movieId;
    private FavouriteMovie favouriteMovie;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
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
        setContentView(R.layout.activity_detail_movie);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewAddDeleteFavouriteMovie = findViewById(R.id.imageViewAddDeleteFavourite);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_ID)) {
            movieId = intent.getIntExtra(MOVIE_ID, -1);
        } else {
            finish();
        }

        movie = viewModel.getMovie(movieId);
        Picasso.get().load(movie.getLargePosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
        checkFavouriteMovie();
    }

    public void onClickAddDeleteFavouriteMovie(View view) {
            if (viewModel.getFavouriteMovies() != null) {
                if (favouriteMovie != null) {
                    viewModel.deleteFavouriteMovie(new FavouriteMovie(viewModel.getMovie(movieId)));
                    checkFavouriteMovie();
                } else {
                    viewModel.insertFavouriteMovie(new FavouriteMovie(viewModel.getMovie(movieId)));
                    checkFavouriteMovie();
                }

            }
    }

    private void checkFavouriteMovie() {
        if (viewModel.getFavouriteMovies() != null) {
            favouriteMovie = viewModel.getFavouriteMovie(movieId);
        }
        if (favouriteMovie != null) {
            imageViewAddDeleteFavouriteMovie.setImageResource(android.R.drawable.btn_star_big_on);
            imageViewAddDeleteFavouriteMovie.setContentDescription(getString(R.string.text_image_view_remove_favourite_movie));
        } else {
            imageViewAddDeleteFavouriteMovie.setImageResource(android.R.drawable.btn_star_big_off);
            imageViewAddDeleteFavouriteMovie.setContentDescription(getString(R.string.text_imageview_add_favourite_movie));
        }
    }


}