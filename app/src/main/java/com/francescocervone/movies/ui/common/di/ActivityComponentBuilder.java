package com.francescocervone.movies.ui.common.di;


public interface ActivityComponentBuilder<M extends ActivityModule, C extends ActivityComponent> {
    ActivityComponentBuilder<M, C> activityModule(M activityModule);

    C build();
}