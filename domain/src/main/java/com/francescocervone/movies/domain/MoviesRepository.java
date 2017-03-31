package com.francescocervone.movies.domain;


import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MovieDetails;

import java.util.List;

import io.reactivex.Flowable;

public interface MoviesRepository {
    Flowable<List<Movie>> getNowPlayingMovies(int page);

    Flowable<List<Movie>> getNowPlayingMoviesCache();

    Flowable<List<Movie>> getMovies(String query, int page);

    Flowable<List<Movie>> getMoviesCache(String query);

    Flowable<MovieDetails> getMovieDetails(String id);
}
