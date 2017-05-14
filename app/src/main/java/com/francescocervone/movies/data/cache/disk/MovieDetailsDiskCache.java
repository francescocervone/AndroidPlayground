package com.francescocervone.movies.data.cache.disk;


import android.content.Context;
import android.support.annotation.Nullable;

import com.francescocervone.movies.data.BuildConfig;
import com.francescocervone.movies.data.cache.MovieDetailsCache;
import com.francescocervone.movies.data.cache.CacheMissException;
import com.francescocervone.movies.domain.model.MovieDetails;
import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;

public class MovieDetailsDiskCache implements MovieDetailsCache {
    private static final int CACHE_SIZE = 1024 * 1024 * 10;
    private static final int VALUES_PER_ENTRY = 1;

    private final Gson mGson;
    @Nullable
    private DiskLruCache mDiskLruCache;

    public MovieDetailsDiskCache(Context context) {
        mGson = new Gson();
        try {
            mDiskLruCache = DiskLruCache.open(context.getCacheDir(), BuildConfig.VERSION_CODE, VALUES_PER_ENTRY, CACHE_SIZE);
        } catch (IOException e) {
            // I don't want to crash if there is a problem.
            // In the worst case the network will be called.
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void put(MovieDetails movieDetails) {
        DiskLruCache.Editor editor = null;
        try {
            if (mDiskLruCache != null) {
                editor = mDiskLruCache.edit(movieDetails.getId());
                editor.set(0, mGson.toJson(movieDetails));
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (editor != null) {
                editor.abortUnlessCommitted();
            }
        }
    }

    @Override
    public synchronized MovieDetails get(String id) throws CacheMissException {
        try {
            if (mDiskLruCache != null) {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(id);
                if (snapshot != null) {
                    String json = snapshot.getString(0);
                    snapshot.close();
                    MovieDetails movie = mGson.fromJson(json, MovieDetails.class);
                    if (movie != null) {
                        return movie;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new CacheMissException();
    }

    @Override
    public void clear() {
        // No-op: this is an LRU cache
    }
}
