package com.francescocervone.movies.data.mapper;


import com.francescocervone.movies.data.entities.GenreEntity;
import com.francescocervone.movies.data.entities.MovieDetailsEntity;
import com.francescocervone.movies.data.entities.MovieEntity;
import com.francescocervone.movies.data.entities.ProductionCompanyEntity;
import com.francescocervone.movies.data.entities.ProductionCountryEntity;
import com.francescocervone.movies.data.entities.SpokenLanguageEntity;
import com.francescocervone.movies.domain.model.Movie;
import com.francescocervone.movies.domain.model.MovieDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovieMapper {
    private static final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w1000";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private final SimpleDateFormat mSimpleDateFormat;

    public MovieMapper() {
        mSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    public Movie map(MovieEntity movieEntity) {
        return new Movie(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getOverview(),
                getImageUrl(movieEntity.getPosterPath()),
                getImageUrl(movieEntity.getBackdropPath()),
                getDate(movieEntity.getReleaseDate()));
    }

    public MovieDetails map(MovieDetailsEntity movieDetailsEntity) {
        return new MovieDetails(
                movieDetailsEntity.getId(),
                movieDetailsEntity.getTitle(),
                movieDetailsEntity.getOverview(),
                getImageUrl(movieDetailsEntity.getPosterPath()),
                getImageUrl(movieDetailsEntity.getBackdropPath()),
                getDate(movieDetailsEntity.getReleaseDate()),
                movieDetailsEntity.getTagline(),
                getGenres(movieDetailsEntity.getGenres()),
                getProductionCompanies(movieDetailsEntity.getProductionCompanies()),
                getProductionCountries(movieDetailsEntity.getProductionCountries()),
                getSpokenLanguages(movieDetailsEntity.getSpokenLanguages()),
                movieDetailsEntity.getVoteAverage(),
                movieDetailsEntity.getVoteCount()
        );
    }

    private List<String> getSpokenLanguages(List<SpokenLanguageEntity> spokenLanguages) {
        List<String> languages = new ArrayList<>();
        for (SpokenLanguageEntity spokenLanguage : spokenLanguages) {
            languages.add(spokenLanguage.getName());
        }
        return languages;
    }

    private List<String> getProductionCountries(List<ProductionCountryEntity> productionCountries) {
        List<String> countries = new ArrayList<>();
        for (ProductionCountryEntity productionCountry : productionCountries) {
            countries.add(productionCountry.getName());
        }
        return countries;
    }

    private List<String> getProductionCompanies(List<ProductionCompanyEntity> productionCompanies) {
        List<String> companies = new ArrayList<>();
        for (ProductionCompanyEntity productionCompany : productionCompanies) {
            companies.add(productionCompany.getName());
        }
        return companies;
    }

    private List<String> getGenres(List<GenreEntity> genreEntities) {
        List<String> genres = new ArrayList<>();
        for (GenreEntity genreEntity : genreEntities) {
            genres.add(genreEntity.getName());
        }
        return genres;
    }

    private long getDate(String date) {
        try {
            return mSimpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private String getImageUrl(String path) {
        if (path != null) {
            return IMAGE_BASE_PATH + path;
        }
        return null;
    }
}
