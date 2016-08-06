package com.commit451.lifeline.sample;

import android.app.Application;

import com.commit451.lifeline.Lifeline;

/**
 * Here is where you would initialize {@link com.commit451.lifeline.Lifeline}
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Lifeline.init(this);
    }
}
