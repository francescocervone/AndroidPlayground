package com.francescocervone.movies.ui.detail.di;


import com.francescocervone.movies.ui.detail.MovieDetailsActivity;
import com.francescocervone.movies.ui.common.di.ApplicationComponent;

import dagger.Component;

@MovieDetailsScope
@Component(dependencies = {ApplicationComponent.class}, modules = {DetailsModule.class})
public interface DetailsComponent {
    void inject(MovieDetailsActivity activity);
}
