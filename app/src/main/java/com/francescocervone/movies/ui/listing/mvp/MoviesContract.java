package com.francescocervone.movies.ui.listing.mvp;


import com.francescocervone.movies.ui.common.mvp.ErrorType;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.ui.common.mvp.BasePresenter;

import java.util.List;

import io.reactivex.Flowable;

public interface MoviesContract {
    interface View {
        void showContentLoader();

        void hideContentLoader();

        void showEmptyView();

        void hideEmptyView();

        void showContentError(ErrorType errorType);

        void hideContentError();

        void clearMovies();

        void appendMovies(List<Movie> movies);

        void showList();

        void hideList();

        void showListLoader();

        void hideListLoader();

        void showListError(ErrorType errorType);

        Flowable<String> observeQuery();

        Flowable<String> observeMovieClicks();

        Flowable<?> observePullToRefresh();

        void openMovieDetails(String movieId);
    }

    interface Presenter extends BasePresenter {
        void load();

        void loadMore();

        void restore(String query);
    }

}
