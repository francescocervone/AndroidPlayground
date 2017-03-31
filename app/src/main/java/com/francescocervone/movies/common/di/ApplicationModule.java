package com.francescocervone.movies.common.di;

import android.app.Application;
import android.content.Context;

import com.francescocervone.movies.Movies;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Movies mApplication;

    public ApplicationModule(Movies application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }
}
