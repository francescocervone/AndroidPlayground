package com.francescocervone.movies.listing.mvp;


import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.usecases.NowPlayingMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.francescocervone.movies.common.TextUtils.isEmpty;

public class Presenter implements MoviesContract.Presenter {
    private CompositeDisposable mRequestsCompositeDisposable = new CompositeDisposable();
    private CompositeDisposable mViewCompositeDisposable = new CompositeDisposable();
    private UseCase<NowPlayingMovies.Request, List<Movie>> mNowPlayingMovies;
    private UseCase<SearchMovies.Request, List<Movie>> mSearchMovies;
    private MoviesContract.View mView;
    private int mCurrentPage = -1;
    private String mQuery;

    @Inject
    public Presenter(UseCase<NowPlayingMovies.Request, List<Movie>> nowPlayingMovies,
                     UseCase<SearchMovies.Request, List<Movie>> searchMovies,
                     MoviesContract.View view) {
        mNowPlayingMovies = nowPlayingMovies;
        mSearchMovies = searchMovies;
        mView = view;
    }

    @Override
    public void start() {
        Disposable queryDisposable = mView.observeQuery()
                .distinctUntilChanged()
                .subscribe(query -> {
                    if (isEmpty(query)) {
                        load();
                    } else {
                        search(query);
                    }
                });

        Disposable movieClicksDisposable = mView.observeMovieClicks()
                .subscribe(movieId -> {
                    mView.openMovieDetails(movieId);
                });
        mViewCompositeDisposable.add(queryDisposable);
        mViewCompositeDisposable.add(movieClicksDisposable);
    }

    @Override
    public void stop() {
        mRequestsCompositeDisposable.clear();
        mViewCompositeDisposable.clear();
    }

    @Override
    public void load() {
        prepareNewSearch();
        Disposable disposable = mNowPlayingMovies.execute(NowPlayingMovies.Request.firstPage())
                .subscribe(this::dispatchPageReceived, throwable -> {
                    dispatchError();
                    throwable.printStackTrace();
                });
        mRequestsCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMore() {
        mView.showListLoader();

        Flowable<List<Movie>> flowable;
        if (isEmpty(mQuery)) {
            flowable = mNowPlayingMovies.execute(NowPlayingMovies.Request.page(++mCurrentPage));
        } else {
            flowable = mSearchMovies.execute(SearchMovies.Request.from(mQuery, ++mCurrentPage));
        }

        Disposable disposable = flowable
                .subscribe(movies -> {
                    mView.appendMovies(movies);
                    mView.hideListLoader();
                }, throwable -> {
                    mView.showListError();
                    mView.hideListLoader();
                });
        mRequestsCompositeDisposable.add(disposable);
    }

    @Override
    public void restore(String query) {
        prepareNewSearch(query);

        Flowable<List<Movie>> flowable;
        if (isEmpty(query)) {
            flowable = mNowPlayingMovies.execute(NowPlayingMovies.Request.fromCache());
        } else {
            flowable = mSearchMovies.execute(SearchMovies.Request.fromCache(query));
        }

        Disposable countDisposable = flowable.count()
                .subscribe(pages -> mCurrentPage = pages.intValue() - 1);

        Disposable pagesDisposable = flowable
                .collectInto(new ArrayList<Movie>(), List::addAll)
                .subscribe(this::dispatchPageReceived, throwable -> {
                    dispatchError();
                    throwable.printStackTrace();
                });

        mRequestsCompositeDisposable.add(countDisposable);
        mRequestsCompositeDisposable.add(pagesDisposable);
    }

    private void search(String query) {
        prepareNewSearch(query);
        Disposable disposable = mSearchMovies.execute(SearchMovies.Request.from(query))
                .subscribe(this::dispatchPageReceived, throwable -> {
                    dispatchError();
                    throwable.printStackTrace();
                });
        mRequestsCompositeDisposable.add(disposable);
    }

    private void prepareNewSearch() {
        prepareNewSearch(null);
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

    private void dispatchPageReceived(List<Movie> movies) {
        mCurrentPage++;
        if (movies.isEmpty()) {
            mView.showEmptyView();
        } else {
            mView.appendMovies(movies);
            mView.showList();
        }
        mView.hideContentLoader();
    }

    private void dispatchError() {
        mView.hideContentLoader();
        mView.showContentError();
    }
}
