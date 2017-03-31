package com.francescocervone.movies.data.cache;


import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;

import java.util.List;

public interface NowPlayingMoviesCache {
    void put(int page, List<Movie> movies);

    List<List<Movie>> get() throws CacheMissException;

    void clear();
}
