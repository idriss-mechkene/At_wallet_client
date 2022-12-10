package com.alliancetechnologie.at_wallet_client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);

        BtnBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backBtn) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}