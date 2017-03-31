package com.francescocervone.movies.data.cache.memory;


import android.support.v4.util.LruCache;

import com.francescocervone.movies.data.cache.MovieDetailsCache;
import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.model.MovieDetails;

public class InMemoryMovieDetailsCache implements MovieDetailsCache {
    private LruCache<String, MovieDetails> mCache = new LruCache<>(20);

    @Override
    public void put(MovieDetails movieDetails) {
        mCache.put(movieDetails.getId(), movieDetails);
    }

    @Override
    public MovieDetails get(String id) throws CacheMissException {
        MovieDetails movieDetails = mCache.get(id);
        if (movieDetails != null) {
            return movieDetails;
        }
        throw new CacheMissException();
    }

    @Override
    public void clear() {
        mCache.evictAll();
    }
}