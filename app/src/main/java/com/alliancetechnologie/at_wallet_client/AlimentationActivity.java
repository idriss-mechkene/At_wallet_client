package com.alliancetechnologie.at_wallet_client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AlimentationActivity extends AppCompatActivity implements View.OnClickListener, ProfileServiceInterface {

    ImageView BtnBack;
    Button BtnAlimentation;
    TextInputLayout layoutAmount;
    TextInputEditText AmountRecharge;
    String amount;
    ProfileServiceImpl profileService;
    CustomerAlertDialog customerAlertDialog;
    Dialog dialogLoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentation);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        profileService = new ProfileServiceImpl(this, this);
    }

    @Override
    public void onSuccessUpdatePassword() {

    }

    @Override
    public void onSuccessUpdateProfile(User user) {

    }

    @Override
    public void onSuccessAlimenterAccount(ResponseAlimentationAccount responseAlimentationAccount) {
        dialogLoder.dismiss();
        RSUserSession.updateBalanceIntoSharedPreferences(responseAlimentationAccount.getSolde(), getApplicationContext());
        customerAlertDialog.showSuccessDialog("Alimentation du Compté avec success ", SettingActivity.class);
    }

    @Override
    public void onError(String msg, int code) {
        Log.e("TAG", "onError: " + msg + " " + code);
        dialogLoder.dismiss();
        if (code == 0) {
            customerAlertDialog.showErrorDialog(msg);
        } else {
            customerAlertDialog.showErrorDialog(code + ": " + msg);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
            case R.id.btn_allimentation:
                amount = AmountRecharge.getText().toString();
                if (Validate()) {
                    dialogLoder = Utils.StartLoader(this);
                    RequestAlimentationAccount requestAlimentationAccount = new RequestAlimentationAccount();
                    requestAlimentationAccount.setAccountId(RSUserSession.getIdAccount(this));
                    requestAlimentationAccount.setAmount(amount.replace(".", ""));
                    profileService.alimentationAccount(RSUserSession.getToken(this), requestAlimentationAccount);
                }
                break;
        }
    }

    private boolean Validate() {
        boolean valid = true;
        if (amount.isEmpty()) {
            layoutAmount.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        return valid;
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        BtnAlimentation = findViewById(R.id.btn_allimentation);
        layoutAmount = findViewById(R.id.input_layout_amount);
        AmountRecharge = findViewById(R.id.amount_recharge);

        BtnBack.setOnClickListener(this);
        BtnAlimentation.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}