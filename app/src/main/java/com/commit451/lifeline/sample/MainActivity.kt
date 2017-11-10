package com.commit451.lifeline.sample

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.commit451.lifeline.Lifeline

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button_dialog).setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Hi")
                    .setMessage("Hello there")
                    .show()
        }
        CheckIfForegroundService.check(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST", "onResume")
        val timeSpentOutsideApp = Lifeline.timeSpentOutsideApp()
        if (timeSpentOutsideApp > 0) {
            Toast.makeText(this@MainActivity, "time spend outside of app: $timeSpentOutsideApp ms", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TEST", "onPause")
    }
}
