package com.commit451.lifeline;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a pulse on your application
 */
public class Lifeline {

    private static TrackedLifecycleCallbacks lifecycleHandler;
    private static List<OnBackgroundedListener> onBackgroundedListeners = new ArrayList<>();
    private static List<OnForegroundedListener> onForegroundedListeners = new ArrayList<>();

    /**
     * Hooks your Application up to this Lifeline
     *
     * @param application application
     */
    public static void init(Application application) {
        lifecycleHandler = new TrackedLifecycleCallbacks();
        application.registerComponentCallbacks(new BackgroundComponentCallbacks2(new InternalBackgroundListener() {
            @Override
            public void onBackgrounded() {
                for (OnBackgroundedListener listener : onBackgroundedListeners) {
                    listener.onBackgrounded();
                }
            }
        }));
        application.registerActivityLifecycleCallbacks(lifecycleHandler);
    }

    /**
     * Check if the app is currently in the foreground
     *
     * @return true if in foreground
     */
    public static boolean isInForeground() {
        checkInit();
        return lifecycleHandler.resumed > lifecycleHandler.paused;
    }

    /**
     * Check if the app is visible
     *
     * @return true if the app is visible
     */
    public static boolean isVisible() {
        checkInit();
        return lifecycleHandler.started > lifecycleHandler.stopped;
    }

    /**
     * Get the last amount of time the user has spent outside the app
     *
     * @return the amount of time in milliseconds. 0 if user has not left the app
     */
    public static long getTimeSpentOutsideApp() {
        checkInit();
        return lifecycleHandler.timeSpentOutsideApp;
    }

    /**
     * Get the currently visible activity. This could be null in certain cases, such as if a
     * dialog is showing on top of the current activity
     *
     * @return the activity that is showing (resumed) or null if none exists
     */
    @Nullable
    public static Activity getCurrentVisibleActivity() {
        if (lifecycleHandler.currentVisibleActivityRef != null) {
            return lifecycleHandler.currentVisibleActivityRef.get();
        }
        return null;
    }

    /**
     * Get the currently created activity. This could be null in certain cases, such as if a
     * no activities have been created (just services or broadcast receivers in existence)
     *
     * @return the latest created activity or null if none exists
     */
    @Nullable
    public static Activity getCurrentCreatedActivity() {
        if (lifecycleHandler.currentCreatedActivityRef != null) {
            return lifecycleHandler.currentCreatedActivityRef.get();
        }
        return null;
    }

    /**
     * Get the currently started activity. This could be null in certain cases, such as if a
     * no activities have been started (just services or broadcast receivers in existence)
     *
     * @return the latest started activity or null if none exists
     */
    @Nullable
    public static Activity getCurrentStartedActivity() {
        if (lifecycleHandler.currentStartedActivityRef != null) {
            return lifecycleHandler.currentStartedActivityRef.get();
        }
        return null;
    }

    public static void register(OnBackgroundedListener listener) {
        onBackgroundedListeners.add(listener);
    }

    public static void unregister(OnBackgroundedListener listener) {
        onBackgroundedListeners.remove(listener);
    }

    public static void register(OnForegroundedListener listener) {
        onForegroundedListeners.add(listener);
    }

    public static void unregister(OnForegroundedListener listener) {
        onForegroundedListeners.remove(listener);
    }

    private static void checkInit() {
        if (lifecycleHandler == null) {
            throw new IllegalStateException("You need to first call `init()` on this class before using it");
        }
    }

    private static class BackgroundComponentCallbacks2 implements ComponentCallbacks2 {

        private InternalBackgroundListener listener;

        public BackgroundComponentCallbacks2(InternalBackgroundListener listener) {
            this.listener = listener;
        }

        @Override
        public void onTrimMemory(int level) {
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                // We're in the Background
                listener.onBackgrounded();
            }
        }

        @Override
        public void onLowMemory() {
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }
    }

    /**
     * Inspired by
     * http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
     */
    private static class TrackedLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        private int resumed;
        private int paused;
        private int started;
        private int stopped;
        private long timeStartedPause;
        private long timeSpentOutsideApp;

        private WeakReference<Activity> currentVisibleActivityRef;
        private WeakReference<Activity> currentCreatedActivityRef;
        private WeakReference<Activity> currentStartedActivityRef;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            currentCreatedActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            currentVisibleActivityRef = null;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            resumed = resumed + 1;
            if (timeStartedPause != 0) {
                timeSpentOutsideApp = System.currentTimeMillis() - timeStartedPause;
                timeStartedPause = 0;
                for (OnForegroundedListener listener : onForegroundedListeners) {
                    listener.onForegrounded();
                }
            }
            currentVisibleActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            paused = paused + 1;
            timeStartedPause = System.currentTimeMillis();
            currentVisibleActivityRef = null;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;
            currentStartedActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;
            currentStartedActivityRef = null;
        }
    }
}
