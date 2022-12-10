package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    BottomNavigationView bottomNavigationView;
    MaterialCardView profileCard, passwordCard, aboutCard, refundCard, ExitCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_card:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.password_card:
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                break;
            case R.id.refund_card:
                startActivity(new Intent(getApplicationContext(), AlimentationActivity.class));
                break;
            case R.id.about_card:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.exit_card:
//                RSUserSession.cancelSession(this);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        profileCard = findViewById(R.id.profile_card);
        passwordCard = findViewById(R.id.password_card);
        refundCard = findViewById(R.id.refund_card);
        aboutCard = findViewById(R.id.about_card);
        ExitCard = findViewById(R.id.exit_card);

        bottomNavigationView.setSelectedItemId(R.id.setting);
        bottomNavigationView.setOnItemSelectedListener(this);
        profileCard.setOnClickListener(this);
        passwordCard.setOnClickListener(this);
        refundCard.setOnClickListener(this);
        aboutCard.setOnClickListener(this);
        ExitCard.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return Utils.NavigationMenus(this, item);
    }

    @Override
    public void onBackPressed() {
    }
}