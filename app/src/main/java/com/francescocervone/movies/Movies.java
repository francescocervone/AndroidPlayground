package com.francescocervone.movies;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.francescocervone.movies.common.di.ActivityComponentBuilder;
import com.francescocervone.movies.common.di.ApplicationComponent;
import com.francescocervone.movies.common.di.ApplicationModule;
import com.francescocervone.movies.common.di.DaggerApplicationComponent;
import com.francescocervone.movies.common.di.MoviesModule;

import java.util.HashMap;
import java.util.Map;

public class Movies extends Application {

    private ApplicationComponent mApplicationComponent;
    private Map<Class<?>, ActivityComponentBuilder> mActivityComponentBuilders;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDaggerComponents();
    }

    private void setupDaggerComponents() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .moviesModule(new MoviesModule())
                .build();
        mActivityComponentBuilders = mApplicationComponent.activityComponentBuilders();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
        mActivityComponentBuilders = mApplicationComponent.activityComponentBuilders();
    }

    public ActivityComponentBuilder getActivityComponentBuilder(Class<? extends Activity> clazz) {
        return mActivityComponentBuilders.get(clazz);
    }

    @VisibleForTesting
    public void putActivityComponentBuilder(Class<? extends Activity> clazz, ActivityComponentBuilder builder) {
        mActivityComponentBuilders = new HashMap<>(mActivityComponentBuilders);
        mActivityComponentBuilders.put(clazz, builder);
    }

    public static Movies from(Context context) {
        return (Movies) context.getApplicationContext();
    }
}
