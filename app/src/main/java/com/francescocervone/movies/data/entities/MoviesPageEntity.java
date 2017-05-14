package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesPageEntity {

    @SerializedName("results")
    private List<MovieEntity> mResults;
    @SerializedName("dates")
    private DatesEntity mDatesEntity;
    @SerializedName("total_pages")
    private double mTotalPages;
    @SerializedName("total_results")
    private double mTotalResults;
    @SerializedName("page")
    private double mPage;

    public List<MovieEntity> getResults() {
        return mResults;
    }

    public DatesEntity getDatesEntity() {
        return mDatesEntity;
    }

    public double getTotalPages() {
        return mTotalPages;
    }

    public double getTotalResults() {
        return mTotalResults;
    }

    public double getPage() {
        return mPage;
    }
}
