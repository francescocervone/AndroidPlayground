package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieEntity {

    @SerializedName("id")
    private String mId;
    @SerializedName("adult")
    private boolean mAdult;
    @SerializedName("video")
    private boolean mVideo;
    @SerializedName("popularity")
    private double mPopularity;
    @SerializedName("vote_count")
    private int mVoteCount;
    @SerializedName("vote_agerage")
    private double mVoteAverage;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("genre_ids")
    private List<Integer> mGenreIds;

    public String getId() {
        return mId;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public List<Integer> getGenreIds() {
        return mGenreIds;
    }
}
