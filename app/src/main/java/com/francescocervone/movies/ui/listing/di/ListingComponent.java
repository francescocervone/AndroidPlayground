package com.francescocervone.movies.ui.listing.di;

import com.francescocervone.movies.ui.common.di.ActivityComponent;
import com.francescocervone.movies.ui.common.di.ActivityComponentBuilder;
import com.francescocervone.movies.ui.listing.MoviesActivity;

import dagger.Subcomponent;

@ListingScope
@Subcomponent(modules = {ListingModule.class})
public interface ListingComponent extends ActivityComponent<MoviesActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ListingModule, ListingComponent> {
    }
}
