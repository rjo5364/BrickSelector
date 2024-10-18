package edu.psu.sweng888.brickselector;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // Make sure this matches your XML filename

        // Delay of .9 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Starts the MainActivity after the delay
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                // Closes the splash screen activity
                finish();
            }
        }, 900);
    }
}