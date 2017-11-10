package com.commit451.lifeline.sample

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

import com.commit451.lifeline.Lifeline

import java.util.concurrent.TimeUnit

/**
 * Check if the app is in the foreground
 */
class CheckIfForegroundService : IntentService(CheckIfForegroundService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        postOnMainThread(Runnable {
            Toast.makeText(this@CheckIfForegroundService, "In foreground: " + Lifeline.isInForeground(), Toast.LENGTH_SHORT).show()
            Log.d("TEST", "Current visible activity: " + Lifeline.currentVisibleActivity())
            Log.d("TEST", "Current created activity: " + Lifeline.currentCreatedActivity())
        })
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10))
            check(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun postOnMainThread(runnable: Runnable) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(runnable)
    }

    companion object {

        fun check(context: Context) {
            val intent = Intent(context, CheckIfForegroundService::class.java)
            context.startService(intent)
        }
    }
}
