package com.francescocervone.movies.common.di;

import android.app.Application;
import android.content.Context;

import com.francescocervone.movies.data.ApiManager;
import com.francescocervone.movies.domain.MoviesDataSource;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MoviesModule.class, SubcomponentsModule.class})
public interface ApplicationComponent {
    Application application();

    Context context();

    ApiManager apiManager();

    MoviesDataSource dataSource();

    Map<Class<?>, ActivityComponentBuilder> activityComponentBuilders();
}
