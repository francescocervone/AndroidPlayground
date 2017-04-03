package com.francescocervone.movies.listing.mvp;


import com.francescocervone.movies.common.mvp.ErrorType;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.MoviesPage;
import com.francescocervone.movies.domain.usecases.FetchNowPlayingMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

import static com.francescocervone.movies.common.TextUtils.isEmpty;

public class MoviesPresenter implements MoviesContract.Presenter {
    private CompositeDisposable mRequestsCompositeDisposable = new CompositeDisposable();
    private CompositeDisposable mViewCompositeDisposable = new CompositeDisposable();

    private UseCase<FetchNowPlayingMovies.Request, MoviesPage> mNowPlayingMovies;
    private UseCase<SearchMovies.Request, MoviesPage> mSearchMovies;
    private MoviesContract.View mView;

    private int mCurrentPage = -1;
    private String mQuery;

    @Inject
    public MoviesPresenter(UseCase<FetchNowPlayingMovies.Request, MoviesPage> nowPlayingMovies,
                           UseCase<SearchMovies.Request, MoviesPage> searchMovies,
                           MoviesContract.View view) {
        mNowPlayingMovies = nowPlayingMovies;
        mSearchMovies = searchMovies;
        mView = view;
    }

    @Override
    public void start() {
        mViewCompositeDisposable.add(mView.observeQuery()
                .distinctUntilChanged()
                .subscribe(this::load));

        mViewCompositeDisposable.add(mView.observeMovieClicks()
                .subscribe(movieId -> mView.openMovieDetails(movieId)));

        mViewCompositeDisposable.add(mView.observePullToRefresh()
                .subscribe(v -> load()));
    }

    @Override
    public void load() {
        load(mQuery);
    }

    private void load(String query) {
        prepareNewSearch(query);

        Flowable<MoviesPage> firstPageFlowable;
        if (isEmpty(query)) {
            FetchNowPlayingMovies.Request request = FetchNowPlayingMovies.Request.firstPage();
            firstPageFlowable = mNowPlayingMovies.execute(request);
        } else {
            SearchMovies.Request request = SearchMovies.Request.from(query);
            firstPageFlowable = mSearchMovies.execute(request);
        }

        mRequestsCompositeDisposable.add(
                firstPageFlowable.subscribe(
                        this::handleFirstPage,
                        this::handleError));
    }

    @Override
    public void loadMore() {
        mView.showListLoader();

        Flowable<MoviesPage> nextPageFlowable;
        if (isEmpty(mQuery)) {
            nextPageFlowable = mNowPlayingMovies.execute(FetchNowPlayingMovies.Request.page(++mCurrentPage));
        } else {
            nextPageFlowable = mSearchMovies.execute(SearchMovies.Request.from(mQuery, ++mCurrentPage));
        }

        mRequestsCompositeDisposable.add(
                nextPageFlowable.subscribe(
                        this::handlePage,
                        this::handleListError));
    }

    @Override
    public void restore(String query) {
        prepareNewSearch(query);

        Flowable<MoviesPage> restoreFlowable;
        if (isEmpty(query)) {
            restoreFlowable = mNowPlayingMovies.execute(FetchNowPlayingMovies.Request.fromCache());
        } else {
            restoreFlowable = mSearchMovies.execute(SearchMovies.Request.fromCache(query));
        }

        mRequestsCompositeDisposable.add(
                restoreFlowable.subscribe(
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

    private void handleFirstPage(MoviesPage page) {
        mCurrentPage = page.getPageNumber();
        if (page.getMovies().isEmpty()) {
            mView.showEmptyView();
        } else {
            handlePage(page);
            mView.showList();
        }
        mView.hideContentLoader();
    }

    private void handlePage(MoviesPage movies) {
        mView.appendMovies(movies.getMovies());
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
