package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.MoviesDataSource;
import com.francescocervone.movies.domain.model.MoviesPage;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public class SearchMovies extends MoviesUseCase<SearchMovies.Request> {

    private MoviesDataSource mDataSource;

    public SearchMovies(Scheduler executionScheduler,
                        Scheduler postExecutionScheduler,
                        MoviesDataSource dataSource) {
        super(executionScheduler, postExecutionScheduler);
        mDataSource = dataSource;
    }

    @Override
    protected Flowable<MoviesPage> observable(final Request params) {
        return mDataSource.getMovies(params.mQuery, params.mPage)
                .map(mapPage(params.mPage));
    }

    public static class Request {
        private String mQuery;
        private int mPage;

        private Request(String query, int page) {
            mQuery = query;
            mPage = page;
        }

        public static Request from(String query) {
            return from(query, 1);
        }

        public static Request from(String query, int page) {
            return new Request(query, page);
        }
    }
}
