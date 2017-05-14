package com.francescocervone.movies.ui.listing.mvp;

import com.francescocervone.movies.TestSchedulerRule;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MoviesPage;
import com.francescocervone.movies.domain.usecases.FetchNowPlayingMovies;
import com.francescocervone.movies.domain.usecases.GetCachedMovies;
import com.francescocervone.movies.domain.usecases.SearchMovies;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MoviesPresenterTest {

    private final TestScheduler mTestScheduler = new TestScheduler();

    @Rule
    public TestRule mTestSchedulerRule = new TestSchedulerRule(mTestScheduler);

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    public MoviesContract.View mView;

    @Mock
    public FetchNowPlayingMovies mFetchNowPlayingMovies;

    @Mock
    public SearchMovies mSearchMovies;

    @Mock
    public GetCachedMovies mGetCachedMovies;

    private MoviesPresenter mPresenterUnderTest;

    private PublishProcessor<String> mQueryProcessor = PublishProcessor.create();

    private PublishProcessor<String> mMovieClickProcessor = PublishProcessor.create();

    private PublishProcessor<Object> mPullToRefreshProcessor = PublishProcessor.create();

    @Before
    public void setUp() throws Exception {
        mPresenterUnderTest = new MoviesPresenter(
                mFetchNowPlayingMovies,
                mSearchMovies,
                mGetCachedMovies,
                mTestScheduler,
                mView);

        when(mView.observeMovieClicks()).thenReturn(mMovieClickProcessor);
        when(mView.observeQuery()).thenReturn(mQueryProcessor);
        when(mView.observePullToRefresh()).thenAnswer(new Answer<PublishProcessor<?>>() {
            @Override
            public PublishProcessor<?> answer(InvocationOnMock invocation) throws Throwable {
                return mPullToRefreshProcessor;
            }
        });

        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.empty());
        when(mSearchMovies.execute(any())).thenReturn(Flowable.empty());
        when(mGetCachedMovies.execute(any())).thenReturn(Flowable.empty());
    }

    @Test
    public void start_observeViewUserInteractions() {
        mPresenterUnderTest.start();

        verify(mView).observeMovieClicks();
        verify(mView).observeQuery();
        verify(mView).observePullToRefresh();
        verifyNoMoreInteractions(mView);
    }

    @Test
    public void start_queryTyped_searchMoviesCalled() {
        mPresenterUnderTest.start();

        String keyword = "The Godfather";
        typeKeyword(keyword);
        waitForQueryDebounce();

        verify(mSearchMovies).execute(SearchMovies.Request.from(keyword));
        verifyNoMoreInteractions(mSearchMovies);
        verifyZeroInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void start_queryTypedTooFast_justOneSearch() {
        mPresenterUnderTest.start();

        String keyword = "The Godfather";

        typeKeyword("a");
        typeKeyword("aa");
        typeKeyword("aaa");
        typeKeyword(keyword);
        waitForQueryDebounce();

        verify(mSearchMovies).execute(SearchMovies.Request.from(keyword));
        verifyNoMoreInteractions(mSearchMovies);
        verifyZeroInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void start_queryTypedTooSlow_moreThanOneSearch() {
        mPresenterUnderTest.start();

        String firstKeyword = "aaa";
        String secondKeyword = "The Godfather";

        typeKeyword("a");
        typeKeyword("aa");
        typeKeyword(firstKeyword);
        waitForQueryDebounce();

        typeKeyword(secondKeyword);
        waitForQueryDebounce();

        InOrder inOrder = inOrder(mSearchMovies);
        inOrder.verify(mSearchMovies).execute(SearchMovies.Request.from(firstKeyword));
        inOrder.verify(mSearchMovies).execute(SearchMovies.Request.from(secondKeyword));
        verifyNoMoreInteractions(mSearchMovies);
        verifyZeroInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void start_pullToRefreshWithoutQuery_nowPlayingMoviesCalled() {
        mPresenterUnderTest.start();

        pullToRefresh();

        verify(mFetchNowPlayingMovies).execute(FetchNowPlayingMovies.Request.page(1));
        verifyNoMoreInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mSearchMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void start_pullToRefreshWithQuery_searchMoviesCalled() {
        mPresenterUnderTest.start();

        String keyword = "The Godfather";
        typeKeyword(keyword);
        waitForQueryDebounce();

        clearInvocations(mSearchMovies);

        pullToRefresh();
        verify(mSearchMovies).execute(SearchMovies.Request.from(keyword));
        verifyNoMoreInteractions(mSearchMovies);
        verifyZeroInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void start_movieClick_openDetails() {
        mPresenterUnderTest.start();
        clearInvocations(mView);

        String movieId = "id1";
        clickMovie(movieId);
        verify(mView).openMovieDetails(movieId);
        verifyNoMoreInteractions(mView);
    }

    @Test
    public void stop_userInteractions_noActions() {
        mPresenterUnderTest.start();
        mPresenterUnderTest.stop();

        String keyword = "The Godfather";
        typeKeyword(keyword);
        waitForQueryDebounce();

        pullToRefresh();

        clickMovie("id1");

        verifyZeroInteractions(mSearchMovies);
    }

    @Test
    public void load_withoutQuery_nowPlayingMoviesCalled() {
        mPresenterUnderTest.load();

        verify(mFetchNowPlayingMovies).execute(FetchNowPlayingMovies.Request.page(1));
        verifyNoMoreInteractions(mFetchNowPlayingMovies);
    }

    @Test
    public void load_withQuery_searchMoviesCalled() {
        mPresenterUnderTest.start();

        String keyword = "The Godfather";
        typeKeyword(keyword);
        waitForQueryDebounce();

        clearInvocations(mSearchMovies);

        mPresenterUnderTest.load();
        verify(mSearchMovies).execute(SearchMovies.Request.from(keyword));
    }

    @Test
    public void load_whenCalled_preparationViewMethodsCalled() {
        mPresenterUnderTest.load();

        verify(mView).clearMovies();
        verify(mView).showContentLoader();
        verify(mView).hideList();
        verify(mView).hideEmptyView();
    }

    @Test
    public void load_withException_showErrorCalled() {
        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.error(new RuntimeException()));
        mPresenterUnderTest.load();

        verify(mView).showContentError(any());
        verify(mView).hideContentLoader();
    }

    @Test
    public void load_withEmptyPage_showEmptyViewCalled() {
        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.just(new MoviesPage(0, Collections.emptyList())));
        mPresenterUnderTest.load();

        verify(mView).showEmptyView();
        verify(mView).hideContentLoader();
    }

    @Test
    public void load_withResults_showMoviesCalled() {
        List<Movie> movies = Collections.singletonList(mock(Movie.class));
        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.just(new MoviesPage(1, movies)));

        mPresenterUnderTest.load();

        verify(mView).appendMovies(movies);
        verify(mView).hideContentLoader();
    }

    @Test
    public void loadMore_withoutQuery_nextPageOfNowPlayingMoviesIsCalled() {
        int pageNumber = 2;
        when(mFetchNowPlayingMovies.execute(any()))
                .thenReturn(Flowable.just(new MoviesPage(pageNumber, Collections.singletonList(mock(Movie.class)))));

        mPresenterUnderTest.load();

        clearInvocations(mFetchNowPlayingMovies);

        mPresenterUnderTest.loadMore();

        verify(mFetchNowPlayingMovies).execute(FetchNowPlayingMovies.Request.page(pageNumber + 1));
        verifyNoMoreInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mSearchMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void loadMore_withQuery_nextPageOfSearchMoviesIsCalled() {
        int pageNumber = 4;
        when(mSearchMovies.execute(any()))
                .thenReturn(Flowable.just(new MoviesPage(pageNumber, Collections.singletonList(mock(Movie.class)))));

        mPresenterUnderTest.start();

        String keyword = "The Godfather";
        typeKeyword(keyword);
        waitForQueryDebounce();

        clearInvocations(mSearchMovies);

        mPresenterUnderTest.loadMore();

        verify(mSearchMovies).execute(SearchMovies.Request.from(keyword, pageNumber + 1));
        verifyNoMoreInteractions(mSearchMovies);
        verifyZeroInteractions(mFetchNowPlayingMovies);
        verifyZeroInteractions(mGetCachedMovies);
    }

    @Test
    public void loadMore_whenCalled_preparationViewMethodsCalled() {
        mPresenterUnderTest.loadMore();

        verify(mView).showListLoader();
    }

    @Test
    public void loadMore_withError_showListErrorCalled() {
        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.error(new RuntimeException()));

        mPresenterUnderTest.loadMore();

        verify(mView).showListError(any());
        verify(mView).hideListLoader();
    }

    @Test
    public void loadMore_withResult_doNothing() {
        List<Movie> movies = Collections.singletonList(mock(Movie.class));
        when(mFetchNowPlayingMovies.execute(any())).thenReturn(Flowable.just(new MoviesPage(1, movies)));

        mPresenterUnderTest.loadMore();

        verify(mView).hideListLoader();
        verify(mView).appendMovies(movies);
    }

    @Test
    public void restore_withoutQuery_getAllCachedResults() {
        mPresenterUnderTest.restore(null);

        verify(mGetCachedMovies).execute(GetCachedMovies.Request.findAll());
    }

    @Test
    public void restore_withQuery_getSearchCachedResults() {
        String keyword = "The Godfather";
        mPresenterUnderTest.restore(keyword);

        verify(mGetCachedMovies).execute(GetCachedMovies.Request.find(keyword));
    }

    private void clickMovie(String movieId) {
        mMovieClickProcessor.onNext(movieId);
        mTestScheduler.triggerActions();
    }

    private void pullToRefresh() {
        mPullToRefreshProcessor.onNext(new Object());
        mTestScheduler.triggerActions();
    }

    private void waitForQueryDebounce() {
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
    }

    private void typeKeyword(String keyword) {
        mQueryProcessor.onNext(keyword);
        mTestScheduler.triggerActions();
    }
}