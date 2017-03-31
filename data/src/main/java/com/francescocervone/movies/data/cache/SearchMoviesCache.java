package com.francescocervone.movies.data.cache;


import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;

import java.util.List;

public interface SearchMoviesCache {
    void put(String query, int page, List<Movie> movies);

    List<List<Movie>> get(String query) throws CacheMissException;

    void clear(String query);
}
