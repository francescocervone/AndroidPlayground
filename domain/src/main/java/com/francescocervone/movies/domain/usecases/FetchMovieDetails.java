package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.MoviesDataSource;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.MovieDetails;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public class FetchMovieDetails extends UseCase<FetchMovieDetails.Request, MovieDetails> {
    private MoviesDataSource mDataSource;

    public FetchMovieDetails(Scheduler executionScheduler, Scheduler postExecutionScheduler,
                             MoviesDataSource dataSource) {
        super(executionScheduler, postExecutionScheduler);

        mDataSource = dataSource;
    }

    @Override
    protected Flowable<MovieDetails> observable(Request params) {
        return mDataSource.getMovieDetails(params.mId);
    }

    public static class Request {
        private String mId;

        private Request(String id) {
            mId = id;
        }

        public static Request fromId(String id) {
            return new Request(id);
        }
    }
}
