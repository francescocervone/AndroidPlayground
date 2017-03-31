package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.MoviesRepository;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.MovieDetails;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public class FetchMovieDetails extends UseCase<FetchMovieDetails.Request, MovieDetails> {
    private MoviesRepository mRepository;

    public FetchMovieDetails(Scheduler executionScheduler, Scheduler postExecutionScheduler,
                             MoviesRepository repository) {
        super(executionScheduler, postExecutionScheduler);

        mRepository = repository;
    }

    @Override
    protected Flowable<MovieDetails> observable(Request params) {
        return mRepository.getMovieDetails(params.mId);
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
