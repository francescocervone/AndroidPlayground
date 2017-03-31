package com.francescocervone.movies.data.cache;


import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.model.MovieDetails;

public interface MovieDetailsCache {
    void put(MovieDetails movieDetails);

    MovieDetails get(String id) throws CacheMissException;

    void clear();
}
