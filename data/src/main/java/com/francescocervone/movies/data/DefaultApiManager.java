package com.francescocervone.movies.data;


import android.support.annotation.NonNull;
import android.util.Log;

import com.francescocervone.movies.data.entities.MovieDetailsEntity;
import com.francescocervone.movies.data.entities.MoviesPageEntity;

import org.reactivestreams.Publisher;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultApiManager implements ApiManager {

    private static final String TAG = DefaultApiManager.class.getName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY_QS = "api_key";
    private static final String API_KEY_VALUE = "594cf1f11605d5eecb156d4a7c22d1e5";

    private final TheMovieDatabase mService;

    public DefaultApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(this::addApiKey)
                        .addInterceptor(createLoggingInterceptor())
                        .build())
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

    private Response addApiKey(Interceptor.Chain chain) throws IOException {

        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter(API_KEY_QS, API_KEY_VALUE)
                .build();

        Request request = chain.request().newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }

    @NonNull
    private HttpLoggingInterceptor createLoggingInterceptor() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.d(TAG, message));

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return loggingInterceptor;
    }
}
