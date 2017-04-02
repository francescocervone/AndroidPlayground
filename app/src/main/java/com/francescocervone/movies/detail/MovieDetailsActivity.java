package com.francescocervone.movies.detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.francescocervone.movies.Movies;
import com.francescocervone.movies.R;
import com.francescocervone.movies.common.mvp.ErrorType;
import com.francescocervone.movies.databinding.ActivityMovieDetailsBinding;
import com.francescocervone.movies.detail.di.DaggerDetailsComponent;
import com.francescocervone.movies.detail.di.DetailsModule;
import com.francescocervone.movies.detail.mvp.MovieDetailsContract;

import javax.inject.Inject;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View {

    public static final String KEY_MOVIE_ID = "movie_id";
    public static final int PROGRESS_CHILD_INDEX = 0;
    public static final int EMPTY_VIEW_CHILD_INDEX = 1;
    public static final int CONTENT_CHILD_INDEX = 2;

    private ActivityMovieDetailsBinding mBinding;

    @Inject
    MovieDetailsContract.Presenter mPresenter;

    public static void launch(Context context, String id) {
        context.startActivity(
                new Intent(context, MovieDetailsActivity.class)
                        .putExtra(KEY_MOVIE_ID, id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        setupToolbar();

        setupSwipeRefreshLayout();

        setupPresenter();

        mPresenter.start();
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }
    }

    private void setupSwipeRefreshLayout() {
        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mBinding.swipeRefreshLayout.setRefreshing(false);
            mPresenter.start();
        });
    }

    private void setupPresenter() {
        DaggerDetailsComponent.builder()
                .applicationComponent(Movies.from(this).getApplicationComponent())
                .detailsModule(new DetailsModule(getIntent().getStringExtra(KEY_MOVIE_ID), this))
                .build().inject(this);
    }

    @Override
    public void setBackdrop(String url) {
        if (mBinding.backdrop != null) {
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .into(mBinding.backdrop);
        }
    }

    @Override
    public void setPoster(String url) {
        if (mBinding.poster != null) {
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .into(mBinding.poster);
        }
    }

    @Override
    public void setTitle(String title) {
        if (mBinding.collapsingToolbarLayout != null) {
            mBinding.collapsingToolbarLayout.setTitle(title);
        } else {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    @Override
    public void showTagline(String tagline) {
        mBinding.tagline.setText(tagline);
    }

    @Override
    public void hideTagline() {
        mBinding.tagline.setVisibility(View.GONE);
    }

    @Override
    public void showOverview(String overview) {
        mBinding.overview.setText(overview);
    }

    @Override
    public void hideOverview() {
        mBinding.overview.setVisibility(View.GONE);
    }

    @Override
    public void showGenres(String genres) {
        mBinding.genres.setText(genres);
    }

    @Override
    public void hideGenres() {
        mBinding.genres.setVisibility(View.GONE);
        mBinding.genresLabel.setVisibility(View.GONE);
    }

    @Override
    public void showCompanies(String companies) {
        mBinding.companies.setText(companies);
    }

    @Override
    public void hideCompanies() {
        mBinding.companies.setVisibility(View.GONE);
        mBinding.companiesLabel.setVisibility(View.GONE);
    }

    @Override
    public void showCountries(String countries) {
        mBinding.countries.setText(countries);
    }

    @Override
    public void hideCountries() {
        mBinding.countries.setVisibility(View.GONE);
        mBinding.countriesLabel.setVisibility(View.GONE);
    }

    @Override
    public void showLanguages(String languages) {
        mBinding.languages.setText(languages);
    }

    @Override
    public void hideLanguages() {
        mBinding.languages.setVisibility(View.GONE);
        mBinding.languagesLabel.setVisibility(View.GONE);
    }

    @Override
    public void showVoteAverage(double voteAverage) {
        mBinding.voteAvg.setText(String.valueOf(voteAverage));
    }

    @Override
    public void hideVoteAverage() {
        mBinding.voteAvg.setVisibility(View.GONE);
        mBinding.voteAvgLabel.setVisibility(View.GONE);
    }

    @Override
    public void showVoteCount(int voteCount) {
        mBinding.voteCount.setText(String.valueOf(voteCount));
    }

    @Override
    public void hideVoteCount() {
        mBinding.voteCount.setVisibility(View.GONE);
        mBinding.voteCountLabel.setVisibility(View.GONE);
    }

    @Override
    public void showLoader() {
        mBinding.viewAnimator.setDisplayedChild(PROGRESS_CHILD_INDEX);
        mBinding.swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void showContent() {
        mBinding.viewAnimator.setDisplayedChild(CONTENT_CHILD_INDEX);
        mBinding.swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showError(ErrorType errorType) {
        mBinding.viewAnimator.setDisplayedChild(EMPTY_VIEW_CHILD_INDEX);
        mBinding.emptyView.setText(getErrorMessage(errorType));
        mBinding.swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    private int getErrorMessage(ErrorType errorType) {
        switch (errorType) {
            case SERVICE_UNAVAILABLE:
                return R.string.service_unavailable;
            case NETWORK:
                return R.string.network_error;
            case GENERIC:
            case BAD_REQUEST:
            default:
                return R.string.generic_error;
        }
    }
}
