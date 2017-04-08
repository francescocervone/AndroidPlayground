package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.MoviesDataSource;
import com.francescocervone.movies.domain.model.MoviesPage;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public class FetchNowPlayingMovies extends MoviesUseCase<FetchNowPlayingMovies.Request> {
    private final MoviesDataSource mDataSource;

    public FetchNowPlayingMovies(Scheduler executionScheduler,
                                 Scheduler postExecutionScheduler,
                                 MoviesDataSource dataSource) {
        super(executionScheduler, postExecutionScheduler);
        mDataSource = dataSource;
    }

    @Override
    protected Flowable<MoviesPage> observable(final Request params) {
        return mDataSource.getNowPlayingMovies(params.mPage)
                .map(mapPage(params.mPage));
    }

    public static class Request {
        private int mPage;

        private Request(int page) {
            mPage = page;
        }

        public static Request firstPage() {
            return page(1);
        }

        public static Request page(int page) {
            return new Request(page);
        }
    }
}
