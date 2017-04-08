package com.francescocervone.movies.common.di;

import android.app.Application;
import android.content.Context;

import com.francescocervone.movies.data.ApiManager;
import com.francescocervone.movies.domain.MoviesDataSource;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MoviesModule.class})
public interface ApplicationComponent {
    Application application();

    Context context();

    ApiManager apiManager();

    MoviesDataSource dataSource();
}
