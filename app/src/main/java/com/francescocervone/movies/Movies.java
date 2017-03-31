package com.francescocervone.movies;


import android.app.Application;
import android.content.Context;

import com.francescocervone.movies.common.di.ApplicationComponent;
import com.francescocervone.movies.common.di.ApplicationModule;
import com.francescocervone.movies.common.di.DaggerApplicationComponent;

public class Movies extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDaggerComponents();
    }

    private void setupDaggerComponents() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Movies from(Context context) {
        return (Movies) context.getApplicationContext();
    }
}
