package com.francescocervone.movies.data;


import com.francescocervone.movies.data.entities.MovieDetailsEntity;
import com.francescocervone.movies.data.entities.MoviesPageEntity;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBService {
    @GET("movie/now_playing")
    Flowable<MoviesPageEntity> nowPlaying(@Query("page") int page);

    @GET("search/movie")
    Flowable<MoviesPageEntity> search(@Query("query") String query, @Query("page") int page);

    @GET("movie/{movie_id}")
    Flowable<MovieDetailsEntity> details(@Path("movie_id") String id);
}
