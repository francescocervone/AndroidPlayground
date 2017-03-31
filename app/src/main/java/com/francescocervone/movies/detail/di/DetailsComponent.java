package com.francescocervone.movies.detail.di;


import com.francescocervone.movies.detail.MovieDetailsActivity;
import com.francescocervone.movies.common.di.ApplicationComponent;

import dagger.Component;

@MovieDetailsScope
@Component(dependencies = {ApplicationComponent.class}, modules = {DetailsModule.class})
public interface DetailsComponent {
    void inject(MovieDetailsActivity activity);
}
