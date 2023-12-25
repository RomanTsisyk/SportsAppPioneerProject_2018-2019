package io.github.smartsportapps.app1.adapter;

import android.os.Looper;

import io.github.smartsportapps.app1.BuildConfig;

class ThreadPreconditions {

    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG && !isMainThread()) {
            throw new IllegalStateException("This method should be called from the Main Thread");
        }
    }

    private static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
