package com.francescocervone.movies.domain;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public abstract class UseCase<S, T> {
    private Scheduler mExecutionScheduler;
    private Scheduler mPostExecutionScheduler;

    public UseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        mExecutionScheduler = executionScheduler;
        mPostExecutionScheduler = postExecutionScheduler;
    }

    public Flowable<T> execute(S params) {
        return observable(params)
                .subscribeOn(mExecutionScheduler)
                .observeOn(mPostExecutionScheduler);
    }

    protected abstract Flowable<T> observable(S params);
}
