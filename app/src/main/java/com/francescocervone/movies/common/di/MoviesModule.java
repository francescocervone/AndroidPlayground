package com.francescocervone.movies.common.di;

import android.content.Context;

import com.francescocervone.movies.data.ApiManager;
import com.francescocervone.movies.data.DefaultApiManager;
import com.francescocervone.movies.data.DefaultMoviesRepository;
import com.francescocervone.movies.data.cache.MovieDetailsCache;
import com.francescocervone.movies.data.cache.NowPlayingMoviesCache;
import com.francescocervone.movies.data.cache.SearchMoviesCache;
import com.francescocervone.movies.data.cache.disk.DbNowPlayingMoviesCache;
import com.francescocervone.movies.data.cache.disk.DbSearchMoviesCache;
import com.francescocervone.movies.data.cache.disk.NowPlayingMoviesDbHelper;
import com.francescocervone.movies.data.cache.disk.SearchMoviewDbHelper;
import com.francescocervone.movies.data.cache.memory.InMemoryMovieDetailsCache;
import com.francescocervone.movies.data.mapper.MovieMapper;
import com.francescocervone.movies.domain.MoviesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesModule {
    @Provides
    @Singleton
    public ApiManager provideApiManager() {
        return new DefaultApiManager();
    }

    @Provides
    @Singleton
    public NowPlayingMoviesDbHelper provideNowPlayingDbHelper(Context context) {
        return new NowPlayingMoviesDbHelper(context);
    }

    @Provides
    @Singleton
    public NowPlayingMoviesCache provideNowPlayingCache(NowPlayingMoviesDbHelper dbHelper) {
        return new DbNowPlayingMoviesCache(dbHelper);
    }

    @Provides
    @Singleton
    public SearchMoviewDbHelper provideSearchMoviewDbHelper(Context context) {
        return new SearchMoviewDbHelper(context);
    }

    @Provides
    @Singleton
    public SearchMoviesCache provideSearchCache(SearchMoviewDbHelper dbHelper) {
        return new DbSearchMoviesCache(dbHelper);
    }

    @Provides
    @Singleton
    public MovieDetailsCache provideMovieDetailsCache() {
        return new InMemoryMovieDetailsCache();
    }

    @Provides
    @Singleton
    public MovieMapper provideMovieMapper() {
        return new MovieMapper();
    }

    @Provides
    @Singleton
    public MoviesRepository provideRepository(ApiManager apiManager,
                                              NowPlayingMoviesCache nowPlayingMoviesCache,
                                              SearchMoviesCache searchMoviesCache,
                                              MovieDetailsCache movieDetailsCache,
                                              MovieMapper mapper) {
        return new DefaultMoviesRepository(apiManager, nowPlayingMoviesCache, searchMoviesCache, movieDetailsCache, mapper);
    }
}
