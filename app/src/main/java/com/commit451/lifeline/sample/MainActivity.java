package com.commit451.lifeline.sample;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.commit451.lifeline.Lifeline;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Hi")
                        .setMessage("Hello there")
                        .show();
            }
        });
        CheckIfForegroundService.check(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TEST", "onResume");
        long timeSpentOutsideApp = Lifeline.getTimeSpentOutsideApp();
        if (timeSpentOutsideApp > 0) {
            Toast.makeText(MainActivity.this, "time spend outside of app: " + timeSpentOutsideApp + " ms", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TEST", "onPause");
    }
}
