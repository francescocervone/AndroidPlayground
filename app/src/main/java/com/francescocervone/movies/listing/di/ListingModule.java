package com.francescocervone.movies.listing.di;

import com.francescocervone.movies.common.di.ActivityModule;
import com.francescocervone.movies.domain.MoviesDataSource;
import com.francescocervone.movies.domain.usecases.FetchNowPlayingMovies;
import com.francescocervone.movies.domain.usecases.GetCachedMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;
import com.francescocervone.movies.listing.MoviesActivity;
import com.francescocervone.movies.listing.mvp.MoviesContract;
import com.francescocervone.movies.listing.mvp.MoviesPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = ListingModule.Presenter.class)
public class ListingModule extends ActivityModule<MoviesActivity> {
    private MoviesActivity mActivity;

    public ListingModule(MoviesActivity activity) {
        super(activity);
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
    public FetchNowPlayingMovies provideNowPlayingUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesDataSource repository) {
        return new FetchNowPlayingMovies(executionScheduler, postExecutionScheduler, repository);
    }

    @Provides
    @ListingScope
    public SearchMovies provideSearchMoviesUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesDataSource repository) {
        return new SearchMovies(executionScheduler, postExecutionScheduler, repository);
    }

    @Provides
    @ListingScope
    public GetCachedMovies provideGetCachedMoviesUseCase(
            @Named("executionScheduler") Scheduler executionScheduler,
            @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
            MoviesDataSource repository) {
        return new GetCachedMovies(executionScheduler, postExecutionScheduler, repository);
    }

    @Module
    public interface Presenter {
        @Binds
        MoviesContract.Presenter providePresenter(MoviesPresenter presenter);
    }
}
