package com.francescocervone.movies.listing.di;

import com.francescocervone.movies.common.di.ActivityComponent;
import com.francescocervone.movies.common.di.ActivityComponentBuilder;
import com.francescocervone.movies.listing.MoviesActivity;

import dagger.Subcomponent;

@ListingScope
@Subcomponent(modules = {ListingModule.class})
public interface ListingComponent extends ActivityComponent<MoviesActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ListingModule, ListingComponent> {
    }
}
