package com.francescocervone.movies.listing.mvp;


import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MoviesPage;
import com.francescocervone.movies.domain.usecases.NowPlayingMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

import static com.francescocervone.movies.common.TextUtils.isEmpty;

public class Presenter implements MoviesContract.Presenter {
    private CompositeDisposable mRequestsCompositeDisposable = new CompositeDisposable();
    private CompositeDisposable mViewCompositeDisposable = new CompositeDisposable();
    private UseCase<NowPlayingMovies.Request, MoviesPage> mNowPlayingMovies;
    private UseCase<SearchMovies.Request, MoviesPage> mSearchMovies;
    private MoviesContract.View mView;
    private int mCurrentPage = -1;
    private String mQuery;

    @Inject
    public Presenter(UseCase<NowPlayingMovies.Request, MoviesPage> nowPlayingMovies,
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
                .subscribe(movieId -> {
                    mView.openMovieDetails(movieId);
                }));
    }

    @Override
    public void load() {
        load(null);
    }

    private void load(String query) {
        prepareNewSearch(query);

        Flowable<MoviesPage> requestFlowable;
        if (isEmpty(query)) {
            NowPlayingMovies.Request request = NowPlayingMovies.Request.firstPage();
            requestFlowable = mNowPlayingMovies.execute(request);
        } else {
            SearchMovies.Request request = SearchMovies.Request.from(query);
            requestFlowable = mSearchMovies.execute(request);
        }

        mRequestsCompositeDisposable.add(
                requestFlowable
                        .subscribe(moviesPage -> firstPageReceived(moviesPage.getMovies()),
                                throwable -> {
                                    dispatchError();
                                    throwable.printStackTrace();
                                }));
    }

    @Override
    public void loadMore() {
        mView.showListLoader();

        Flowable<MoviesPage> flowable;
        if (isEmpty(mQuery)) {
            flowable = mNowPlayingMovies.execute(NowPlayingMovies.Request.page(++mCurrentPage));
        } else {
            flowable = mSearchMovies.execute(SearchMovies.Request.from(mQuery, ++mCurrentPage));
        }

        mRequestsCompositeDisposable.add(flowable
                .subscribe(page -> {
                    mView.appendMovies(page.getMovies());
                    mView.hideListLoader();
                }, throwable -> {
                    mView.showListError();
                    mView.hideListLoader();
                }));
    }

    @Override
    public void restore(String query) {
        prepareNewSearch(query);

        Flowable<MoviesPage> flowable;
        if (isEmpty(query)) {
            flowable = mNowPlayingMovies.execute(NowPlayingMovies.Request.fromCache());
        } else {
            flowable = mSearchMovies.execute(SearchMovies.Request.fromCache(query));
        }

        mRequestsCompositeDisposable.add(flowable
                .subscribe(moviesPage -> {
                    mCurrentPage = moviesPage.getPageNumber();
                    firstPageReceived(moviesPage.getMovies());
                }, throwable -> {
                    dispatchError();
                    throwable.printStackTrace();
                }));
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

    private void firstPageReceived(List<Movie> movies) {
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

    @Override
    public void stop() {
        mRequestsCompositeDisposable.clear();
        mViewCompositeDisposable.clear();
    }
}
