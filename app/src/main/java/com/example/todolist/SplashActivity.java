package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        TextView textViewLogo = findViewById(R.id.textViewLogo);

        // Animation
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textViewLogo.startAnimation(fadeInAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the splash screen timeout, start the main activity
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Close the splash activity to prevent going back to it
            }
        }, SPLASH_TIMEOUT);
    }
}