package com.commit451.lifeline.sample;

import android.app.Application;
import android.widget.Toast;

import com.commit451.lifeline.Lifeline;
import com.commit451.lifeline.OnBackgroundedListener;
import com.commit451.lifeline.OnForegroundedListener;

/**
 * Here is where you would initialize {@link com.commit451.lifeline.Lifeline}
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Lifeline.init(this);
        Lifeline.register(new OnBackgroundedListener() {
            @Override
            public void onBackgrounded() {
                Toast.makeText(App.this, "On backgrounded", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        Lifeline.register(new OnForegroundedListener() {
            @Override
            public void onForegrounded() {
                Toast.makeText(App.this, "On foregrounded", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
