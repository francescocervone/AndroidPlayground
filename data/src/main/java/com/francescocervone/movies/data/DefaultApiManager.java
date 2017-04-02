package com.francescocervone.movies.data;


import android.support.annotation.NonNull;

import com.francescocervone.movies.data.entities.MovieDetailsEntity;
import com.francescocervone.movies.data.entities.MoviesPageEntity;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultApiManager implements ApiManager {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String QUERY_API_KEY = "api_key";
    public static final String API_KEY = "594cf1f11605d5eecb156d4a7c22d1e5";
    private final TheMovieDatabase mService;

    public DefaultApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(chain -> {
                            HttpUrl url = chain.request().url()
                                    .newBuilder()
                                    .addQueryParameter(QUERY_API_KEY, API_KEY)
                                    .build();
                            Request request = chain.request().newBuilder()
                                    .url(url)
                                    .build();
                            return chain.proceed(request);
                        }).build())
                .baseUrl(BASE_URL)
                .build();

        mService = retrofit.create(TheMovieDatabase.class);
    }

    @Override
    public Flowable<MoviesPageEntity> nowPlayingMovies(int page) {
        return mService.nowPlaying(page)
                .onErrorResumeNext(handleError());
    }

    @Override
    public Flowable<MoviesPageEntity> searchMovies(String query, int page) {
        return mService.search(query, page)
                .onErrorResumeNext(handleError());
    }

    @Override
    public Flowable<MovieDetailsEntity> movieDetails(String id) {
        return mService.details(id)
                .onErrorResumeNext(handleError());
    }

    @NonNull
    private <T> Function<Throwable, Publisher<? extends T>> handleError() {
        return throwable -> Flowable.error(ErrorHandler.convert(throwable));
    }
}
