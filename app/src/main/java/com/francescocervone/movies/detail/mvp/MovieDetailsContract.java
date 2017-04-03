package com.francescocervone.movies.detail.mvp;


import com.francescocervone.movies.common.mvp.BasePresenter;
import com.francescocervone.movies.common.mvp.ErrorType;

import io.reactivex.Flowable;

public interface MovieDetailsContract {
    interface View {
        void setBackdrop(String url);

        void setPoster(String url);

        void setTitle(String title);

        void showTagline(String tagline);

        void hideTagline();

        void showOverview(String overview);

        void hideOverview();

        void showGenres(String genres);

        void hideGenres();

        void showCompanies(String companies);

        void hideCompanies();

        void showCountries(String countries);

        void hideCountries();

        void showLanguages(String languages);

        void hideLanguages();

        void showVoteAverage(double voteAverage);

        void hideVoteAverage();

        void showVoteCount(int voteCount);

        void hideVoteCount();

        void showLoader();

        void showContent();

        void showError(ErrorType errorType);

        Flowable<?> observePullToRefresh();
    }

    interface Presenter extends BasePresenter {

    }
}
