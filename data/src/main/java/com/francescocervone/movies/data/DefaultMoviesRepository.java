package com.francescocervone.movies.data;


import android.support.annotation.NonNull;

import com.francescocervone.movies.data.cache.MovieDetailsCache;
import com.francescocervone.movies.data.cache.NowPlayingMoviesCache;
import com.francescocervone.movies.data.cache.SearchMoviesCache;
import com.francescocervone.movies.data.entities.MoviesPageEntity;
import com.francescocervone.movies.data.mapper.MovieMapper;
import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.MoviesRepository;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MovieDetails;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class DefaultMoviesRepository implements MoviesRepository {
    private final ApiManager mApiManager;
    private final NowPlayingMoviesCache mNowPlayingMoviesCache;
    private final SearchMoviesCache mSearchMoviesCache;
    private final MovieDetailsCache mMovieDetailsCache;
    private final MovieMapper mMapper;

    public DefaultMoviesRepository(ApiManager apiManager,
                                   NowPlayingMoviesCache nowPlayingMoviesCache,
                                   SearchMoviesCache searchMoviesCache,
                                   MovieDetailsCache movieDetailsCache,
                                   MovieMapper mapper) {
        mApiManager = apiManager;
        mNowPlayingMoviesCache = nowPlayingMoviesCache;
        mSearchMoviesCache = searchMoviesCache;
        mMovieDetailsCache = movieDetailsCache;
        mMapper = mapper;
    }

    @Override
    public Flowable<List<Movie>> getNowPlayingMovies(int page) {
        if (isFirstPage(page)) {
            mNowPlayingMoviesCache.clear();
        }
        return mApiManager.nowPlayingMovies(page)
                .flatMap(map())
                .doOnNext(movies -> mNowPlayingMoviesCache.put(page, movies));

    }

    @Override
    public Flowable<List<List<Movie>>> getNowPlayingMoviesCache() {
        return Flowable.fromCallable(mNowPlayingMoviesCache::get);
    }

    @Override
    public Flowable<List<Movie>> getMovies(String query, int page) {
        if (isFirstPage(page)) {
            mSearchMoviesCache.clear(query);
        }
        return mApiManager.searchMovies(query, page)
                .flatMap(map())
                .doOnNext(movies -> mSearchMoviesCache.put(query, page, movies));
    }

    @Override
    public Flowable<List<List<Movie>>> getMoviesCache(String query) {
        return Flowable.fromCallable(() -> mSearchMoviesCache.get(query));
    }

    @Override
    public Flowable<MovieDetails> getMovieDetails(String id) {
        return Flowable.fromCallable(() -> mMovieDetailsCache.get(id))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof CacheMissException) {
                        return mApiManager.movieDetails(id)
                                .map(mMapper::map)
                                .doOnNext(mMovieDetailsCache::put);
                    }
                    return Flowable.error(throwable);
                });
    }

    @NonNull
    private Function<MoviesPageEntity, Publisher<List<Movie>>> map() {
        return moviesPageEntity -> Flowable.fromIterable(moviesPageEntity.getResults())
                .map(mMapper::map)
                .toList()
                .toFlowable();
    }

    private boolean isFirstPage(int page) {
        return page == 1;
    }
}
