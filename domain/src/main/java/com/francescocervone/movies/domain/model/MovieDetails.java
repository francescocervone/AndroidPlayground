package com.francescocervone.movies.domain.model;


import java.util.Collections;
import java.util.List;

public class MovieDetails extends Movie {
    private String mTagLine;
    private List<String> mGenres;
    private List<String> mProductionCompanies;
    private List<String> mProductionCountries;
    private List<String> mSpokenLanguages;
    private double mVoteAverage;
    private int mVoteCount;

    public MovieDetails(String id, String title, String overview, String posterUrl, String backdropUrl,
                        long releaseDate, String tagLine, List<String> genres,
                        List<String> productionCompanies, List<String> productionCountries,
                        List<String> spokenLanguages, double voteAverage, int voteCount) {
        super(id, title, overview, posterUrl, backdropUrl, releaseDate);
        mTagLine = tagLine;
        mGenres = genres;
        mProductionCompanies = productionCompanies;
        mProductionCountries = productionCountries;
        mSpokenLanguages = spokenLanguages;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
    }

    public String getTagLine() {
        return mTagLine;
    }

    public List<String> getGenres() {
        return Collections.unmodifiableList(mGenres);
    }

    public List<String> getProductionCompanies() {
        return Collections.unmodifiableList(mProductionCompanies);
    }

    public List<String> getProductionCountries() {
        return Collections.unmodifiableList(mProductionCountries);
    }

    public List<String> getSpokenLanguages() {
        return Collections.unmodifiableList(mSpokenLanguages);
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDetails)) return false;
        if (!super.equals(o)) return false;

        MovieDetails that = (MovieDetails) o;

        if (Double.compare(that.mVoteAverage, mVoteAverage) != 0) return false;
        if (mVoteCount != that.mVoteCount) return false;
        if (mTagLine != null ? !mTagLine.equals(that.mTagLine) : that.mTagLine != null)
            return false;
        if (mGenres != null ? !mGenres.equals(that.mGenres) : that.mGenres != null) return false;
        if (mProductionCompanies != null ? !mProductionCompanies.equals(that.mProductionCompanies) : that.mProductionCompanies != null)
            return false;
        if (mProductionCountries != null ? !mProductionCountries.equals(that.mProductionCountries) : that.mProductionCountries != null)
            return false;
        return mSpokenLanguages != null ? mSpokenLanguages.equals(that.mSpokenLanguages) : that.mSpokenLanguages == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (mTagLine != null ? mTagLine.hashCode() : 0);
        result = 31 * result + (mGenres != null ? mGenres.hashCode() : 0);
        result = 31 * result + (mProductionCompanies != null ? mProductionCompanies.hashCode() : 0);
        result = 31 * result + (mProductionCountries != null ? mProductionCountries.hashCode() : 0);
        result = 31 * result + (mSpokenLanguages != null ? mSpokenLanguages.hashCode() : 0);
        temp = Double.doubleToLongBits(mVoteAverage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mVoteCount;
        return result;
    }

    @Override
    public String toString() {
        return "FetchMovieDetails{" +
                "mTagLine='" + mTagLine + '\'' +
                ", mGenres=" + mGenres +
                ", mProductionCompanies=" + mProductionCompanies +
                ", mProductionCountries=" + mProductionCountries +
                ", mSpokenLanguages=" + mSpokenLanguages +
                ", mVoteAverage=" + mVoteAverage +
                ", mVoteCount=" + mVoteCount +
                "} " + super.toString();
    }
}
