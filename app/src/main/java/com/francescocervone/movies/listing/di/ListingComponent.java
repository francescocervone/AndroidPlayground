package com.francescocervone.movies.listing.di;

import com.francescocervone.movies.common.di.ApplicationComponent;
import com.francescocervone.movies.listing.MoviesActivity;

import dagger.Component;

@Component(dependencies = {ApplicationComponent.class}, modules = {ListingModule.class})
@ListingScope
public interface ListingComponent {
    void inject(MoviesActivity activity);
}
