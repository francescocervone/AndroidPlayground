package com.francescocervone.movies.listing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.francescocervone.movies.Movies;
import com.francescocervone.movies.R;
import com.francescocervone.movies.common.EndlessScrollListener;
import com.francescocervone.movies.common.mvp.ErrorType;
import com.francescocervone.movies.databinding.ActivityMoviesBinding;
import com.francescocervone.movies.detail.MovieDetailsActivity;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.listing.di.DaggerListingComponent;
import com.francescocervone.movies.listing.di.ListingComponent;
import com.francescocervone.movies.listing.di.ListingModule;
import com.francescocervone.movies.listing.mvp.MoviesContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.PublishProcessor;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    public static final String KEY_QUERY = "query";

    private ActivityMoviesBinding mBinding;

    private MoviesAdapter mAdapter;

    private EndlessScrollListener mScrollListener;

    private PublishProcessor<String> mQueryProcessor = PublishProcessor.create();

    private PublishProcessor<Object> mPullToRefreshProcessor = PublishProcessor.create();

    @Inject
    MoviesContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies);

        setSupportActionBar(mBinding.toolbar);

        setupPresenter();

        setupRecyclerView();

        setupSwipeRefreshLayout();

        mPresenter.start();
        if (savedInstanceState == null) {
            mPresenter.load();
        } else {
            mPresenter.restore(savedInstanceState.getString(KEY_QUERY));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // I put this in the onStart() instead of the onCreate in order to avoid to receive an event
        // for the restored SearchView.
        setupSearchView();
    }

    private void setupPresenter() {
        ListingComponent listingComponent = DaggerListingComponent.builder()
                .applicationComponent(Movies.from(this).getApplicationComponent())
                .listingModule(new ListingModule(this))
                .build();
        listingComponent.inject(this);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);

        mScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                mBinding.recyclerView.post(() -> mPresenter.loadMore());
            }
        };
        mBinding.recyclerView.addOnScrollListener(mScrollListener);
    }

    private void setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mQueryProcessor.onNext(newText);
                return true;
            }
        });
    }

    private void setupSwipeRefreshLayout() {
        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mBinding.swipeRefreshLayout.setRefreshing(false);
            mPullToRefreshProcessor.onNext(new Object());
        });
    }

    @NonNull
    private String getQuery() {
        return mBinding.searchView.getQuery().toString();
    }

    @Override
    public void showContentLoader() {
        mBinding.progress.setVisibility(View.VISIBLE);
        mBinding.swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void hideContentLoader() {
        mBinding.progress.setVisibility(View.GONE);
        mBinding.swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void showEmptyView() {
        mBinding.emptyView.setVisibility(View.VISIBLE);
        mBinding.emptyView.setText(R.string.no_results);
    }

    @Override
    public void hideEmptyView() {
        mBinding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showContentError(ErrorType errorType) {
        mBinding.emptyView.setVisibility(View.VISIBLE);
        mBinding.emptyView.setText(getErrorMessage(errorType));
    }

    @Override
    public void hideContentError() {
        mBinding.emptyView.setVisibility(View.GONE);
    }

    @Override
    public void clearMovies() {
        mAdapter.clear();
        mBinding.recyclerView.scrollToPosition(0);
        mScrollListener.resetState();
    }

    @Override
    public void appendMovies(List<Movie> movies) {
        mAdapter.addAll(movies);
    }

    @Override
    public void showList() {
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showListLoader() {
        mAdapter.setLoading(true);
    }

    @Override
    public void hideListLoader() {
        mAdapter.setLoading(false);
    }

    @Override
    public void showListError(ErrorType errorType) {
        int error = getErrorMessage(errorType);
        Snackbar.make(mBinding.recyclerView, error, Snackbar.LENGTH_LONG).show();
        mScrollListener.resetState();
    }

    @Override
    public Flowable<String> observeQuery() {
        return mQueryProcessor.skip(1)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<String> observeMovieClicks() {
        return mAdapter.observeMovieClicks();
    }

    @Override
    public Flowable<?> observePullToRefresh() {
        return mPullToRefreshProcessor;
    }

    @Override
    public void openMovieDetails(String movieId) {
        MovieDetailsActivity.launch(this, movieId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_QUERY, getQuery());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    private int getErrorMessage(ErrorType errorType) {
        // The view shouldn't have "if" or "switch" at all, but since I have to decide only which
        // string to show, I decided to do this.
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
