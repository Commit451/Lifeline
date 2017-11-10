package com.commit451.lifeline.sample

import android.app.Application
import android.widget.Toast

import com.commit451.lifeline.Lifeline

/**
 * Here is where you would initialize [com.commit451.lifeline.Lifeline]
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Lifeline.init(this)
        Lifeline.registerOnBackgroundedListener {
            Toast.makeText(this@App, "On backgrounded", Toast.LENGTH_SHORT)
                    .show()
        }
        Lifeline.registerOnForegroundedListener {
            Toast.makeText(this@App, "On foregrounded", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}
