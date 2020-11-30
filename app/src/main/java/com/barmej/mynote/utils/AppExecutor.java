package com.barmej.mynote.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;

    private final Executor executor;

    public AppExecutor(Executor executor) {
        this.executor = executor;
    }

    public static AppExecutor getsInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new AppExecutor(
                            Executors.newSingleThreadExecutor()
                    );
                }
            }
        }
        return sInstance;
    }

    public Executor getExecutor() {
        return executor;
    }
}
