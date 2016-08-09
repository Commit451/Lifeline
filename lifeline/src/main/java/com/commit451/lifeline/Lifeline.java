package com.commit451.lifeline;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Keeps a pulse on your application
 */
public class Lifeline {

    private static MyLifecycleHandler mLifecycleHandler;

    /**
     * Hooks your Application up to this Lifeline
     * @param application application
     */
    public static void init(Application application) {
        mLifecycleHandler = new MyLifecycleHandler();
        application.registerActivityLifecycleCallbacks(mLifecycleHandler);
    }

    /**
     * Check if the app is currently in the foreground
     *
     * @return true if in foreground
     */
    public static boolean isInForeground() {
        return mLifecycleHandler.resumed > mLifecycleHandler.paused;
    }

    /**
     * Check if the app is visible
     *
     * @return true if the app is visible
     */
    public static boolean isVisible() {
        return mLifecycleHandler.started > mLifecycleHandler.stopped;
    }

    /**
     * Get the last amount of time the user has spent outside the app
     *
     * @return the amount of time in milliseconds. 0 if user has not left the app
     */
    public static long getTimeSpentOutsideApp() {
        return mLifecycleHandler.timeSpentOutsideApp;
    }

    /**
     * Inspired by
     * http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
     */
    private static class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

        private int resumed;
        private int paused;
        private int started;
        private int stopped;
        private long timeStartedPause;
        private long timeSpentOutsideApp;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            resumed = resumed + 1;
            if (timeStartedPause != 0) {
                timeSpentOutsideApp = System.currentTimeMillis() - timeStartedPause;
                timeStartedPause = 0;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            paused = paused + 1;
            timeStartedPause = System.currentTimeMillis();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;
        }
    }
}
