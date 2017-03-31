package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.CacheMissException;
import com.francescocervone.movies.domain.MoviesRepository;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.Movie;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class SearchMovies extends UseCase<SearchMovies.Request, List<Movie>> {

    private MoviesRepository mRepository;

    public SearchMovies(Scheduler executionScheduler,
                        Scheduler postExecutionScheduler,
                        MoviesRepository repository) {
        super(executionScheduler, postExecutionScheduler);
        mRepository = repository;
    }

    @Override
    protected Flowable<List<Movie>> observable(final Request params) {
        if (params.mFromCache) {
            return mRepository.getMoviesCache(params.mQuery)
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends List<Movie>>>() {
                        @Override
                        public Publisher<? extends List<Movie>> apply(@NonNull Throwable throwable) throws Exception {
                            if (throwable instanceof CacheMissException) {
                                return observable(Request.from(params.mQuery));
                            }
                            return Flowable.error(throwable);
                        }
                    });
        }
        return mRepository.getMovies(params.mQuery, params.mPage);
    }

    public static class Request {
        private String mQuery;
        private int mPage;
        private boolean mFromCache;

        private Request(String query, int page) {
            mQuery = query;
            mPage = page;
        }

        private Request(String query, boolean fromCache) {
            mQuery = query;
            mFromCache = fromCache;
        }

        public static Request from(String query) {
            return from(query, 1);
        }

        public static Request from(String query, int page) {
            return new Request(query, page);
        }

        public static Request fromCache(String query) {
            return new Request(query, true);
        }
    }
}
