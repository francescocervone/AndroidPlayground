package com.francescocervone.movies.domain.usecases;


import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MoviesPage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public abstract class MoviesUseCase<S> extends UseCase<S, MoviesPage> {
    public MoviesUseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
    }

    protected Function<List<List<Movie>>, MoviesPage> mapListOfPagesToSinglePage() {
        return new Function<List<List<Movie>>, MoviesPage>() {
            @Override
            public MoviesPage apply(@NonNull List<List<Movie>> pages) throws Exception {
                List<Movie> movies = new ArrayList<>();
                for (List<Movie> page : pages) {
                    movies.addAll(page);
                }
                return new MoviesPage(pages.size(), movies);
            }
        };
    }

    protected Function<List<Movie>, MoviesPage> mapPage(final int page) {
        return new Function<List<Movie>, MoviesPage>() {
            @Override
            public MoviesPage apply(@NonNull List<Movie> movies) throws Exception {
                return new MoviesPage(page, movies);
            }
        };
    }
}
