package com.francescocervone.movies.ui.listing.mvp;


import com.francescocervone.movies.ui.common.mvp.ErrorType;
import com.francescocervone.movies.domain.model.MoviesPage;
import com.francescocervone.movies.domain.usecases.FetchNowPlayingMovies;
import com.francescocervone.movies.domain.usecases.GetCachedMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

import static com.francescocervone.movies.ui.common.TextUtils.isEmpty;

public class MoviesPresenter implements MoviesContract.Presenter {
    private CompositeDisposable mRequestsCompositeDisposable = new CompositeDisposable();
    private CompositeDisposable mViewCompositeDisposable = new CompositeDisposable();

    private FetchNowPlayingMovies mNowPlayingMovies;
    private SearchMovies mSearchMovies;
    private GetCachedMovies mGetCachedMovies;
    private Scheduler mPostExecutionScheduler;
    private MoviesContract.View mView;

    private int mCurrentPage = 0;
    private String mQuery;

    @Inject
    public MoviesPresenter(FetchNowPlayingMovies nowPlayingMovies,
                           SearchMovies searchMovies,
                           GetCachedMovies getCachedMovies,
                           @Named("postExecutionScheduler") Scheduler postExecutionScheduler,
                           MoviesContract.View view) {
        mNowPlayingMovies = nowPlayingMovies;
        mSearchMovies = searchMovies;
        mGetCachedMovies = getCachedMovies;
        mPostExecutionScheduler = postExecutionScheduler;
        mView = view;
    }

    @Override
    public void start() {
        mViewCompositeDisposable.add(mView.observeQuery()
                .distinctUntilChanged()
                .throttleLast(1, TimeUnit.SECONDS)
                .observeOn(mPostExecutionScheduler)
                .subscribe(this::load));

        mViewCompositeDisposable.add(mView.observeMovieClicks()
                .observeOn(mPostExecutionScheduler)
                .subscribe(movieId -> mView.openMovieDetails(movieId)));

        mViewCompositeDisposable.add(mView.observePullToRefresh()
                .observeOn(mPostExecutionScheduler)
                .subscribe(v -> load()));
    }

    @Override
    public void load() {
        load(mQuery);
    }

    private void load(String query) {
        prepareNewSearch(query);

        mRequestsCompositeDisposable.add(
                getNextPageFlowable().subscribe(
                        this::handleFirstPage,
                        this::handleError));
    }

    @Override
    public void loadMore() {
        mView.showListLoader();

        mRequestsCompositeDisposable.add(
                getNextPageFlowable().subscribe(
                        this::handlePage,
                        this::handleListError));
    }

    @Override
    public void restore(String query) {
        prepareNewSearch(query);

        GetCachedMovies.Request request = isEmpty(mQuery) ?
                GetCachedMovies.Request.findAll() :
                GetCachedMovies.Request.find(mQuery);

        mRequestsCompositeDisposable.add(
                mGetCachedMovies.execute(request)
                        .switchIfEmpty(getNextPageFlowable())
                        .subscribe(
                                this::handleFirstPage,
                                this::handleError));
    }

    private void prepareNewSearch(String query) {
        mRequestsCompositeDisposable.clear();
        mQuery = query;
        mCurrentPage = 0;
        mView.hideContentError();
        mView.hideEmptyView();
        mView.hideList();
        mView.showContentLoader();
        mView.clearMovies();
    }

    private Flowable<MoviesPage> getNextPageFlowable() {
        Flowable<MoviesPage> nextPageFlowable;
        if (isEmpty(mQuery)) {
            nextPageFlowable = mNowPlayingMovies.execute(FetchNowPlayingMovies.Request.page(mCurrentPage + 1));
        } else {
            nextPageFlowable = mSearchMovies.execute(SearchMovies.Request.from(mQuery, mCurrentPage + 1));
        }
        return nextPageFlowable;
    }

    private void handleFirstPage(MoviesPage page) {
        if (page.getMovies().isEmpty()) {
            mView.showEmptyView();
        } else {
            handlePage(page);
            mView.showList();
        }
        mView.hideContentLoader();
    }

    private void handlePage(MoviesPage page) {
        mCurrentPage = page.getPageNumber();
        mView.appendMovies(page.getMovies());
        mView.hideListLoader();
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        mView.hideContentLoader();
        mView.showContentError(ErrorType.fromThrowable(throwable));
    }

    private void handleListError(Throwable throwable) {
        throwable.printStackTrace();
        mView.showListError(ErrorType.fromThrowable(throwable));
        mView.hideListLoader();
    }

    @Override
    public void stop() {
        mRequestsCompositeDisposable.clear();
        mViewCompositeDisposable.clear();
    }
}
