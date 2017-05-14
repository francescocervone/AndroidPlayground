package com.francescocervone.movies.domain.model;


import java.util.List;

public class MoviesPage {
    private int mPageNumber;
    private List<Movie> mMovies;

    public MoviesPage(int pageNumber, List<Movie> movies) {
        mPageNumber = pageNumber;
        mMovies = movies;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }
}
