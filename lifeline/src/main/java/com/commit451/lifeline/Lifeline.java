package com.commit451.lifeline;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Keeps track of global lifecycle things
 */
public class Lifeline {

    private static MyLifecycleHandler mLifecycleHandler;

    public static void init(Application application) {
        mLifecycleHandler = new MyLifecycleHandler();
        application.registerActivityLifecycleCallbacks(mLifecycleHandler);
    }

    public static boolean isInForeground() {
        return mLifecycleHandler.resumed > mLifecycleHandler.paused;
    }

    public static boolean isVisible() {
        return mLifecycleHandler.started > mLifecycleHandler.stopped;
    }

    public static long getTimeSpentOutsideApp() {
        return mLifecycleHandler.timeSpentOutsideApp;
    }

    /**
     * Inspired by
     * http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
     */
    private static class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
        // I use four separate variables here. You can, of course, just use two and
        // increment/decrement them instead of using four and incrementing them all.
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
            ++resumed;
            if (timeStartedPause != 0) {
                timeSpentOutsideApp = System.currentTimeMillis() - timeStartedPause;
                timeStartedPause = 0;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            ++paused;
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
