package com.francescocervone.movies.ui.detail.di;

import com.francescocervone.movies.ui.detail.mvp.MovieDetailsContract;
import com.francescocervone.movies.ui.detail.mvp.MovieDetailsPresenter;
import com.francescocervone.movies.domain.MoviesDataSource;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.MovieDetails;
import com.francescocervone.movies.domain.usecases.FetchMovieDetails;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = {DetailsModule.Presenter.class})
public class DetailsModule {
    private String mMovieId;
    private MovieDetailsContract.View mView;

    public DetailsModule(String movieId, MovieDetailsContract.View view) {
        mMovieId = movieId;
        mView = view;
    }

    @Provides
    @MovieDetailsScope
    public String provideMovieId() {
        return mMovieId;
    }

    @Provides
    @MovieDetailsScope
    public MovieDetailsContract.View provideView() {
        return mView;
    }

    @Provides
    @MovieDetailsScope
    @Named("executionScheduler")
    public Scheduler provideExecutionScheduler() {
        return Schedulers.io();
    }

    @Provides
    @MovieDetailsScope
    @Named("postExecutionScheduler")
    public Scheduler providePostExecutionScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @MovieDetailsScope
    public UseCase<FetchMovieDetails.Request, MovieDetails> provideUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesDataSource repository) {
        return new FetchMovieDetails(executionScheduler, postExecutionScheduler, repository);
    }

    @Module
    public interface Presenter {
        @Binds
        MovieDetailsContract.Presenter providePresenter(MovieDetailsPresenter presenter);
    }
}
