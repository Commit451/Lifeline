package com.commit451.lifeline.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.commit451.lifeline.Lifeline;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckIfForegroundService.check(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long timeSpentOutsideApp = Lifeline.getTimeSpentOutsideApp();
        if (timeSpentOutsideApp > 0) {
            Toast.makeText(MainActivity.this, "time spend outside of app: " + timeSpentOutsideApp + " ms", Toast.LENGTH_SHORT).show();
        }
    }
}
