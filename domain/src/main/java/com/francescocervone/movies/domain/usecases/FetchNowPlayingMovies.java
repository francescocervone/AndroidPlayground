package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.exceptions.CacheMissException;
import com.francescocervone.movies.domain.MoviesRepository;
import com.francescocervone.movies.domain.model.MoviesPage;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class FetchNowPlayingMovies extends MoviesUseCase<FetchNowPlayingMovies.Request> {
    private final MoviesRepository mRepository;

    public FetchNowPlayingMovies(Scheduler executionScheduler,
                                 Scheduler postExecutionScheduler,
                                 MoviesRepository repository) {
        super(executionScheduler, postExecutionScheduler);
        mRepository = repository;
    }

    @Override
    protected Flowable<MoviesPage> observable(final Request params) {
        if (params.mFromCache) {
            return mRepository.getNowPlayingMoviesCache()
                    .map(mapListOfPagesToSinglePage())
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends MoviesPage>>() {
                        @Override
                        public Publisher<? extends MoviesPage> apply(@NonNull Throwable throwable) throws Exception {
                            if (throwable instanceof CacheMissException) {
                                return observable(Request.firstPage());
                            }
                            return Flowable.error(throwable);
                        }
                    });

        }
        return mRepository.getNowPlayingMovies(params.mPage)
                .map(mapPage(params.mPage));
    }

    public static class Request {
        private int mPage;

        private boolean mFromCache;

        private Request(int page) {
            mPage = page;
        }

        private Request(boolean fromCache) {
            mFromCache = fromCache;
        }

        public static Request firstPage() {
            return page(1);
        }

        public static Request page(int page) {
            return new Request(page);
        }

        public static Request fromCache() {
            return new Request(true);
        }
    }
}
