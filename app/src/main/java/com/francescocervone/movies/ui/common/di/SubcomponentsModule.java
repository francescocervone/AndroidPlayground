package com.francescocervone.movies.ui.common.di;


import com.francescocervone.movies.ui.listing.MoviesActivity;
import com.francescocervone.movies.ui.listing.di.ListingComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {ListingComponent.class})
public abstract class SubcomponentsModule {
    @Binds
    @IntoMap
    @ClassKey(MoviesActivity.class)
    public abstract ActivityComponentBuilder moviesActivityComponentBuilder(ListingComponent.Builder builder);
}
