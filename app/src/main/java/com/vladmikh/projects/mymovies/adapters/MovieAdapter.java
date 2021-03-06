package com.vladmikh.projects.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vladmikh.projects.mymovies.R;
import com.vladmikh.projects.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private List<Movie> movies;
    private OnMovieClickListener onMovieClickListener;
    private OnReachEndMoviesListener onReachEndMoviesListener;

    public MovieAdapter() {
        movies = new ArrayList<>();
    }

    public interface OnMovieClickListener {
        void onMovieClick(int position);
    }
    public interface OnReachEndMoviesListener {
        void onReachEndMovies();
    }

    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    public void setOnReachEndMoviesListener(OnReachEndMoviesListener onReachEndMoviesListener) {
        this.onReachEndMoviesListener = onReachEndMoviesListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewHolder);
        if (movies.size() >= 20 && position > movies.size() - 3 && onReachEndMoviesListener != null) {
            onReachEndMoviesListener.onReachEndMovies();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewHolder;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewHolder = itemView.findViewById(R.id.smallImageView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (onMovieClickListener != null) {
                        onMovieClickListener.onMovieClick(getAdapterPosition());
                    }
                }
            });

        }

    }
}
