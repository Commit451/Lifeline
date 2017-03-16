package com.commit451.lifeline.sample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.commit451.lifeline.Lifeline;

import java.util.concurrent.TimeUnit;

/**
 * Check if the app is in the foreground
 */
public class CheckIfForegroundService extends IntentService {

    public static void check(Context context) {
        Intent intent = new Intent(context, CheckIfForegroundService.class);
        context.startService(intent);
    }

    public CheckIfForegroundService() {
        super(CheckIfForegroundService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        postOnMainThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CheckIfForegroundService.this, "In foreground: " + Lifeline.isInForeground(), Toast.LENGTH_SHORT).show();
                Log.d("TEST", "Current visible activity: " + Lifeline.getCurrentVisibleActivity());
                Log.d("TEST", "Current created activity: " + Lifeline.getCurrentCreatedActivity());
            }
        });
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            check(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postOnMainThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
