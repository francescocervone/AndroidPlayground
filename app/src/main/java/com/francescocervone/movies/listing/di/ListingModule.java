package com.francescocervone.movies.listing.di;

import com.francescocervone.movies.domain.MoviesRepository;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.usecases.NowPlayingMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;
import com.francescocervone.movies.listing.MoviesActivity;
import com.francescocervone.movies.listing.mvp.MoviesContract;

import java.util.List;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = ListingModule.Presenter.class)
public class ListingModule {
    private MoviesActivity mActivity;

    public ListingModule(MoviesActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ListingScope
    public MoviesContract.View provideView() {
        return mActivity;
    }

    @Provides
    @ListingScope
    @Named("executionScheduler")
    public Scheduler provideExecutionScheduler() {
        return Schedulers.io();
    }

    @Provides
    @ListingScope
    @Named("postExecutionScheduler")
    public Scheduler providePostExecutionScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @ListingScope
    public UseCase<NowPlayingMovies.Request, List<Movie>> provideNowPlayingUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesRepository repository) {
        return new NowPlayingMovies(executionScheduler, postExecutionScheduler, repository);
    }

    @Provides
    @ListingScope
    public UseCase<SearchMovies.Request, List<Movie>> provideSearchMoviesUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesRepository repository) {
        return new SearchMovies(executionScheduler, postExecutionScheduler, repository);
    }

    @Module
    public interface Presenter {
        @Binds
        MoviesContract.Presenter providePresenter(com.francescocervone.movies.listing.mvp.Presenter presenter);
    }
}
