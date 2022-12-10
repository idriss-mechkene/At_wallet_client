package com.alliancetechnologie.at_wallet_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alliancetechnologie.at_wallet_client.utils.Constants;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView logo;
//    Animation animFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
//        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.translate);
//        logo.setAnimation(animFadeIn);
        new Handler().postDelayed(() -> {
            //This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }, Constants.SPLASH_TIME_OUT);
    }
}