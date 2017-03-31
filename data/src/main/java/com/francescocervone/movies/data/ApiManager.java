package com.francescocervone.movies.data;


import com.francescocervone.movies.data.entities.MovieDetailsEntity;
import com.francescocervone.movies.data.entities.MoviesPageEntity;

import io.reactivex.Flowable;

public interface ApiManager {
    Flowable<MoviesPageEntity> nowPlayingMovies(int page);

    Flowable<MoviesPageEntity> searchMovies(String query, int page);

    Flowable<MovieDetailsEntity> movieDetails(String id);
}
