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

public class NowPlayingMovies extends UseCase<NowPlayingMovies.Request, List<Movie>> {
    private final MoviesRepository mRepository;

    public NowPlayingMovies(Scheduler executionScheduler,
                            Scheduler postExecutionScheduler,
                            MoviesRepository repository) {
        super(executionScheduler, postExecutionScheduler);
        mRepository = repository;
    }

    @Override
    protected Flowable<List<Movie>> observable(final Request params) {
        if (params.mFromCache) {
            return mRepository.getNowPlayingMoviesCache()
                    .onErrorResumeNext(new Function<Throwable, Publisher<? extends List<Movie>>>() {
                        @Override
                        public Publisher<? extends List<Movie>> apply(@NonNull Throwable throwable) throws Exception {
                            if (throwable instanceof CacheMissException) {
                                return observable(Request.firstPage());
                            }
                            return Flowable.error(throwable);
                        }
                    });
        }
        return mRepository.getNowPlayingMovies(params.mPage);
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
            return new Request(1);
        }

        public static Request page(int page) {
            return new Request(page);
        }

        public static Request fromCache() {
            return new Request(true);
        }
    }
}
