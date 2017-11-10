package com.commit451.lifeline.sample

import android.app.Application
import android.widget.Toast

import com.commit451.lifeline.Lifeline
import com.commit451.lifeline.OnBackgroundedListener
import com.commit451.lifeline.OnForegroundedListener

/**
 * Here is where you would initialize [com.commit451.lifeline.Lifeline]
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Lifeline.init(this)
        Lifeline.register(OnBackgroundedListener {
            Toast.makeText(this@App, "On backgrounded", Toast.LENGTH_SHORT)
                    .show()
        })
        Lifeline.register(OnForegroundedListener {
            Toast.makeText(this@App, "On foregrounded", Toast.LENGTH_SHORT)
                    .show()
        })
    }
}
