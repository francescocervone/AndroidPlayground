package com.francescocervone.movies.detail.mvp;


import com.francescocervone.movies.common.TextUtils;
import com.francescocervone.movies.domain.UseCase;
import com.francescocervone.movies.domain.model.MovieDetails;
import com.francescocervone.movies.domain.usecases.FetchMovieDetails;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.francescocervone.movies.common.TextUtils.isEmpty;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {
    public static final String DEFAULT_SEPARATOR = ", ";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private UseCase<FetchMovieDetails.Request, MovieDetails> mUseCase;
    private MovieDetailsContract.View mView;
    private String mMovieId;

    @Inject
    public MovieDetailsPresenter(
            UseCase<FetchMovieDetails.Request, MovieDetails> useCase,
            MovieDetailsContract.View view,
            String movieId) {
        mUseCase = useCase;
        mView = view;
        mMovieId = movieId;
    }

    @Override
    public void start() {
        mView.showLoader();
        Disposable disposable = mUseCase.execute(FetchMovieDetails.Request.fromId(mMovieId))
                .subscribe(this::feedView, throwable -> {
                    mView.showError();
                    throwable.printStackTrace();
                }, () -> mView.showContent());
        mCompositeDisposable.add(disposable);
    }

    private void feedView(MovieDetails movieDetails) {
        mView.setBackdrop(movieDetails.getBackdropUrl());
        mView.setPoster(movieDetails.getPosterUrl());
        mView.setTitle(movieDetails.getTitle());

        if (!isEmpty(movieDetails.getTagLine())) {
            mView.showTagline(movieDetails.getTagLine());
        } else {
            mView.hideTagline();
        }

        if (!isEmpty(movieDetails.getOverview())) {
            mView.showOverview(movieDetails.getOverview());
        } else {
            mView.hideOverview();
        }

        if (!movieDetails.getGenres().isEmpty()) {
            mView.showGenres(TextUtils.join(DEFAULT_SEPARATOR, movieDetails.getGenres()));
        } else {
            mView.hideGenres();
        }

        if (!movieDetails.getProductionCompanies().isEmpty()) {
            mView.showCompanies(TextUtils.join(DEFAULT_SEPARATOR, movieDetails.getProductionCompanies()));
        } else {
            mView.hideCompanies();
        }

        if (!movieDetails.getProductionCountries().isEmpty()) {
            mView.showCountries(TextUtils.join(DEFAULT_SEPARATOR, movieDetails.getProductionCountries()));
        } else {
            mView.hideCountries();
        }

        if (!movieDetails.getSpokenLanguages().isEmpty()) {
            mView.showLanguages(TextUtils.join(DEFAULT_SEPARATOR, movieDetails.getSpokenLanguages()));
        } else {
            mView.hideLanguages();
        }

        if (movieDetails.getVoteAverage() > 0d) {
            mView.showVoteAverage(movieDetails.getVoteAverage());
        } else {
            mView.hideVoteAverage();
        }

        if (movieDetails.getVoteCount() > 0) {
            mView.showVoteCount(movieDetails.getVoteCount());
        } else {
            mView.hideVoteCount();
        }
    }

    @Override
    public void stop() {
        mCompositeDisposable.clear();
    }
}
