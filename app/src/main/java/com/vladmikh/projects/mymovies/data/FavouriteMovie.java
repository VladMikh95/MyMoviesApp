package com.vladmikh.projects.mymovies.data;

import androidx.room.Entity;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie{


    public FavouriteMovie(int id, int voteCount, String title, String originalTitle, String overview, String posterPath, String largePosterPath, String backdropPath, double voteAverage, String releaseDate) {
        super(id, voteCount, title, originalTitle, overview, posterPath, largePosterPath, backdropPath, voteAverage, releaseDate);
    }

    public FavouriteMovie(Movie movie) {
        super(movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getPosterPath(),
                movie.getLargePosterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseDate());
    }
}
