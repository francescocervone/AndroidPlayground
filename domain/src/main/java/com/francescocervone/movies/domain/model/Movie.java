package com.francescocervone.movies.domain.model;


public class Movie {
    private String mId;
    private String mTitle;
    private String mOverview;
    private String mPosterUrl;
    private String mBackdropUrl;
    private long mReleaseDate;

    public Movie(String id, String title, String overview, String posterUrl, String backdropUrl,
                 long releaseDate) {
        mId = id;
        mTitle = title;
        mOverview = overview;
        mPosterUrl = posterUrl;
        mBackdropUrl = backdropUrl;
        mReleaseDate = releaseDate;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackdropUrl() {
        return mBackdropUrl;
    }

    public long getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (mReleaseDate != movie.mReleaseDate) return false;
        if (mId != null ? !mId.equals(movie.mId) : movie.mId != null) return false;
        if (mTitle != null ? !mTitle.equals(movie.mTitle) : movie.mTitle != null) return false;
        if (mOverview != null ? !mOverview.equals(movie.mOverview) : movie.mOverview != null)
            return false;
        if (mPosterUrl != null ? !mPosterUrl.equals(movie.mPosterUrl) : movie.mPosterUrl != null)
            return false;
        return mBackdropUrl != null ? mBackdropUrl.equals(movie.mBackdropUrl) : movie.mBackdropUrl == null;

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mOverview != null ? mOverview.hashCode() : 0);
        result = 31 * result + (mPosterUrl != null ? mPosterUrl.hashCode() : 0);
        result = 31 * result + (mBackdropUrl != null ? mBackdropUrl.hashCode() : 0);
        result = 31 * result + (int) (mReleaseDate ^ (mReleaseDate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mPosterUrl='" + mPosterUrl + '\'' +
                ", mBackdropUrl='" + mBackdropUrl + '\'' +
                ", mReleaseDate=" + mReleaseDate +
                '}';
    }
}
