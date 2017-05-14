package com.francescocervone.movies.ui.common.di;


import android.app.Activity;

import dagger.MembersInjector;

public interface ActivityComponent<A extends Activity> extends MembersInjector<A> {
}
