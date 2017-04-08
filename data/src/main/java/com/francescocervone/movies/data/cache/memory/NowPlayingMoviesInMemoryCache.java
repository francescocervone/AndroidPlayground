package com.francescocervone.movies.data.cache.memory;


import com.francescocervone.movies.data.cache.NowPlayingMoviesCache;
import com.francescocervone.movies.data.cache.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class NowPlayingMoviesInMemoryCache implements NowPlayingMoviesCache {
    private SortedMap<Integer, List<Movie>> mPagesCache = new TreeMap<>();

    @Override
    public void put(int page, List<Movie> movies) {
        mPagesCache.put(page, movies);
    }

    @Override
    public List<List<Movie>> get() throws CacheMissException {
        List<List<Movie>> pages = new ArrayList<>();
        for (Map.Entry<Integer, List<Movie>> entry : mPagesCache.entrySet()) {
            pages.add(Collections.unmodifiableList(entry.getValue()));
        }
        if (pages.isEmpty()) {
            throw new CacheMissException();
        }
        return Collections.unmodifiableList(pages);
    }

    @Override
    public void clear() {
        mPagesCache.clear();
    }
}
