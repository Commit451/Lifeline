package com.commit451.lifeline

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle

import java.lang.ref.WeakReference

/**
 * Keeps a pulse on your application
 */
object Lifeline {

    private var lifecycleHandler: TrackedLifecycleCallbacks? = null
    private val onBackgroundedListeners = mutableListOf<OnBackgroundedListener>()
    private val onForegroundedListeners = mutableListOf<OnForegroundedListener>()

    /**
     * Hooks your Application up to this Lifeline
     *
     * @param application application
     */
    fun init(application: Application) {
        lifecycleHandler = TrackedLifecycleCallbacks()
        application.registerComponentCallbacks(BackgroundComponentCallbacks2({
            for (listener in onBackgroundedListeners) {
                listener.invoke()
            }
        }))
        application.registerActivityLifecycleCallbacks(lifecycleHandler)
    }

    /**
     * Check if the app is currently in the foreground
     *
     * @return true if in foreground
     */
    fun isInForeground(): Boolean {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.resumed > lifecycleHandler.paused
    }

    /**
     * Check if the app is visible
     *
     * @return true if the app is visible
     */
    fun isVisible(): Boolean {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.started > lifecycleHandler.stopped
    }

    /**
     * Get the last amount of time the user has spent outside the app
     *
     * @return the amount of time in milliseconds. 0 if user has not left the app
     */
    fun timeSpentOutsideApp(): Long {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.timeSpentOutsideApp
    }

    /**
     * Get the currently visible activity. This could be null in certain cases, such as if a
     * dialog is showing on top of the current activity
     *
     * @return the activity that is showing (resumed) or null if none exists
     */
    fun currentVisibleActivity(): Activity? {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.currentVisibleActivityRef?.get()
    }

    /**
     * Get the currently created activity. This could be null in certain cases, such as if a
     * no activities have been created (just services or broadcast receivers in existence)
     *
     * @return the latest created activity or null if none exists
     */
    fun currentCreatedActivity(): Activity? {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.currentCreatedActivityRef?.get()
    }

    /**
     * Get the currently started activity. This could be null in certain cases, such as if a
     * no activities have been started (just services or broadcast receivers in existence)
     *
     * @return the latest started activity or null if none exists
     */
    fun currentStartedActivity(): Activity? {
        val lifecycleHandler = lifecycleHandler()
        return lifecycleHandler.currentStartedActivityRef?.get()
    }

    /**
     * Register for a callback when the app is backgrounded
     */
    fun registerOnBackgroundedListener(listener: OnBackgroundedListener) {
        onBackgroundedListeners.add(listener)
    }

    /**
     * Unregister the callback for when the app is backgrounded
     */
    fun unregisterOnBackgroundedListener(listener: OnBackgroundedListener) {
        onBackgroundedListeners.remove(listener)
    }

    /**
     * Register for a callback when the app is foregrounded
     */
    fun registerOnForegroundedListener(listener: OnForegroundedListener) {
        onForegroundedListeners.add(listener)
    }

    /**
     * Unregister the callback for when the app is foregrounded
     */
    fun unregisterOnForegroundedListener(listener: OnForegroundedListener) {
        onForegroundedListeners.remove(listener)
    }

    private fun lifecycleHandler(): TrackedLifecycleCallbacks {
        return lifecycleHandler ?: throw IllegalStateException("You need to call init() before accessing Lifeline")
    }

    /**
     * What a great class name
     */
    private class BackgroundComponentCallbacks2(private val onBackgrounded: () -> Unit) : ComponentCallbacks2 {

        override fun onTrimMemory(level: Int) {
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                // We're in the Background
                onBackgrounded.invoke()
            }
        }

        override fun onLowMemory() {}

        override fun onConfigurationChanged(newConfig: Configuration) {}
    }

    /**
     * Inspired by
     * http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
     */
    internal class TrackedLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        internal var resumed: Int = 0
        internal var paused: Int = 0
        internal var started: Int = 0
        internal var stopped: Int = 0
        private var timeStartedPause: Long = 0
        internal var timeSpentOutsideApp: Long = 0

        internal var currentVisibleActivityRef: WeakReference<Activity>? = null
        internal var currentCreatedActivityRef: WeakReference<Activity>? = null
        internal var currentStartedActivityRef: WeakReference<Activity>? = null

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            currentCreatedActivityRef = WeakReference(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            currentVisibleActivityRef = null
        }

        override fun onActivityResumed(activity: Activity) {
            resumed = resumed + 1
            if (timeStartedPause != 0L) {
                timeSpentOutsideApp = System.currentTimeMillis() - timeStartedPause
                timeStartedPause = 0
                for (listener in onForegroundedListeners) {
                    listener.invoke()
                }
            }
            currentVisibleActivityRef = WeakReference(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            paused = paused + 1
            timeStartedPause = System.currentTimeMillis()
            currentVisibleActivityRef = null
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityStarted(activity: Activity) {
            ++started
            currentStartedActivityRef = WeakReference(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            ++stopped
            currentStartedActivityRef = null
        }
    }
}

typealias OnBackgroundedListener = () -> Unit
typealias OnForegroundedListener = () -> Unit
