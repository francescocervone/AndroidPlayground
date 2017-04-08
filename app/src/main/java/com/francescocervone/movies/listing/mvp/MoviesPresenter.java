package com.francescocervone.movies.listing.mvp;


import com.francescocervone.movies.common.mvp.ErrorType;
import com.francescocervone.movies.domain.model.MoviesPage;
import com.francescocervone.movies.domain.usecases.FetchNowPlayingMovies;
import com.francescocervone.movies.domain.usecases.GetCachedMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;

import static com.francescocervone.movies.common.TextUtils.isEmpty;

public class MoviesPresenter implements MoviesContract.Presenter {
    private CompositeDisposable mRequestsCompositeDisposable = new CompositeDisposable();
    private CompositeDisposable mViewCompositeDisposable = new CompositeDisposable();

    private FetchNowPlayingMovies mNowPlayingMovies;
    private SearchMovies mSearchMovies;
    private GetCachedMovies mGetCachedMovies;
    private MoviesContract.View mView;

    private int mCurrentPage = -1;
    private String mQuery;

    @Inject
    public MoviesPresenter(FetchNowPlayingMovies nowPlayingMovies,
                           SearchMovies searchMovies,
                           GetCachedMovies getCachedMovies,
                           MoviesContract.View view) {
        mNowPlayingMovies = nowPlayingMovies;
        mSearchMovies = searchMovies;
        mGetCachedMovies = getCachedMovies;
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

        mRequestsCompositeDisposable.add(
                getFirstPageFlowable().subscribe(
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
                        .switchIfEmpty(getFirstPageFlowable())
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

    private Flowable<MoviesPage> getFirstPageFlowable() {
        Flowable<MoviesPage> firstPageFlowable;
        if (isEmpty(mQuery)) {
            FetchNowPlayingMovies.Request request = FetchNowPlayingMovies.Request.firstPage();
            firstPageFlowable = mNowPlayingMovies.execute(request);
        } else {
            SearchMovies.Request request = SearchMovies.Request.from(mQuery);
            firstPageFlowable = mSearchMovies.execute(request);
        }
        return firstPageFlowable;
    }

    private Flowable<MoviesPage> getNextPageFlowable() {
        Flowable<MoviesPage> nextPageFlowable;
        if (isEmpty(mQuery)) {
            nextPageFlowable = mNowPlayingMovies.execute(FetchNowPlayingMovies.Request.page(++mCurrentPage));
        } else {
            nextPageFlowable = mSearchMovies.execute(SearchMovies.Request.from(mQuery, ++mCurrentPage));
        }
        return nextPageFlowable;
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
