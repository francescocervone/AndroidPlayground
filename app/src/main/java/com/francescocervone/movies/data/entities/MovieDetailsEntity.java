package com.francescocervone.movies.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetailsEntity {
    @SerializedName("id")
    private String mId;
    @SerializedName("imdb_od")
    private String mImdbId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("tagline")
    private String mTagline;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("runtime")
    private double mRuntime;
    @SerializedName("vote_count")
    private int mVoteCount;
    @SerializedName("vote_average")
    private double mVoteAverage;
    @SerializedName("revenue")
    private double mRevenue;
    @SerializedName("genres")
    private List<GenreEntity> mGenres;
    @SerializedName("production_countries")
    private List<ProductionCountryEntity> mProductionCountries;
    @SerializedName("production_companies")
    private List<ProductionCompanyEntity> mProductionCompanies;
    @SerializedName("spoken_languages")
    private List<SpokenLanguageEntity> mSpokenLanguages;
    @SerializedName("homepage")
    private String mHomepage;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("budget")
    private double mBudget;
    @SerializedName("popularity")
    private double mPopularity;
    @SerializedName("adult")
    private boolean mAdult;
    @SerializedName("video")
    private boolean mVideo;

    public String getId() {
        return mId;
    }

    public String getImdbId() {
        return mImdbId;
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

    public String getTagline() {
        return mTagline;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public double getRuntime() {
        return mRuntime;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public double getRevenue() {
        return mRevenue;
    }

    public List<GenreEntity> getGenres() {
        return mGenres;
    }

    public List<ProductionCountryEntity> getProductionCountries() {
        return mProductionCountries;
    }

    public List<ProductionCompanyEntity> getProductionCompanies() {
        return mProductionCompanies;
    }

    public List<SpokenLanguageEntity> getSpokenLanguages() {
        return mSpokenLanguages;
    }

    public String getHomepage() {
        return mHomepage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getBudget() {
        return mBudget;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public boolean isAdult() {
        return mAdult;
    }

    public boolean isVideo() {
        return mVideo;
    }
}
