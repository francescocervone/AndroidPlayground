package com.francescocervone.movies.ui.common.di;


import android.app.Activity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ActivityModule<T extends Activity> {
    protected final T activity;

    public ActivityModule(T activity) {
        this.activity = activity;
    }

    @Provides
    public T provideActivity() {
        return activity;
    }
}
