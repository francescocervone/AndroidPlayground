package com.francescocervone.movies.data.cache.memory;


import com.francescocervone.movies.data.cache.SearchMoviesCache;
import com.francescocervone.movies.domain.exceptions.CacheMissException;
import com.francescocervone.movies.domain.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class InMemorySearchMoviesCache implements SearchMoviesCache {
    private Map<String, SortedMap<Integer, List<Movie>>> mSearchesCache = new HashMap<>();

    @Override
    public void put(String query, int page, List<Movie> movies) {
        SortedMap<Integer, List<Movie>> sortedMap;
        if (mSearchesCache.containsKey(query)) {
            sortedMap = mSearchesCache.get(query);
        } else {
            sortedMap = new TreeMap<>();
            mSearchesCache.put(query, sortedMap);
        }
        sortedMap.put(page, movies);
    }

    @Override
    public List<List<Movie>> get(String query) throws CacheMissException {
        SortedMap<Integer, List<Movie>> pagesMap = mSearchesCache.get(query);
        if (pagesMap == null) {
            throw new CacheMissException();
        }

        List<List<Movie>> movies = new ArrayList<>();
        for (Map.Entry<Integer, List<Movie>> entry : pagesMap.entrySet()) {
            movies.add(Collections.unmodifiableList(entry.getValue()));
        }
        if (movies.isEmpty()) {
            throw new CacheMissException();
        }
        return Collections.unmodifiableList(movies);
    }

    @Override
    public void clear(String query) {
        if (mSearchesCache.containsKey(query)) {
            mSearchesCache.get(query).clear();
        }
    }
}
