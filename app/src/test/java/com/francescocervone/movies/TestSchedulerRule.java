package com.francescocervone.movies;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

public class TestSchedulerRule implements TestRule {
    private TestScheduler mTestScheduler = new TestScheduler();

    public TestSchedulerRule(TestScheduler testScheduler) {
        mTestScheduler = testScheduler;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> mTestScheduler);
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> mTestScheduler);
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> mTestScheduler);
                RxJavaPlugins.setSingleSchedulerHandler(scheduler -> mTestScheduler);
                base.evaluate();
                RxJavaPlugins.reset();
            }
        };
    }
}
