package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestRestPassword;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener, AuthServiceInterface {

    ImageView BtnBack;
    Button BtnRestPassword;
    TextInputEditText NewPassword, ConfirmPassword;
    TextInputLayout layoutNewPassword, layoutConfirmPassword;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;
    AuthServiceImpl authServiceImpl;
    String confirmPassword, newPassword, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        authServiceImpl = new AuthServiceImpl(getApplicationContext(), this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("Email");
        }
    }

    @Override
    public void onSuccessLogin(User user) {

    }

    @Override
    public void onSuccessRegistration(ResponseVerification responseVerification) {

    }

    @Override
    public void onCompleteVerifyRegistration() {

    }

    @Override
    public void onSuccessForgetPassword(ResponseVerification responseVerification) {

    }

    @Override
    public void onSuccessResendCode(ResponseVerification responseVerification) {

    }

    @Override
    public void onCompleteRestPassword() {
        Log.e("TAG", "onCompleteRestPassword: ");
        dialogLoder.dismiss();
        customerAlertDialog.showSuccessDialog("Votre Mot de passe a été modifié avec succès", LoginActivity.class);
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
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.btn_reinirialiser:
                newPassword = Objects.requireNonNull(NewPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(ConfirmPassword.getText()).toString();
                if (Validate()) {
                    dialogLoder = Utils.StartLoader(this);
                    RequestRestPassword requestRestPassword = new RequestRestPassword();
                    requestRestPassword.setEmail(email);
                    requestRestPassword.setPassword(newPassword);
                    requestRestPassword.setConfirmPassword(confirmPassword);
                    authServiceImpl.RestPassword(requestRestPassword);
                }
                break;
        }
    }

    private boolean Validate() {
        boolean valide = true;
        if (newPassword.isEmpty()) {
            layoutNewPassword.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutNewPassword.setError(null);
        }
        if (confirmPassword.isEmpty()) {
            layoutConfirmPassword.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutConfirmPassword.setError(null);
        }
        if ((!newPassword.isEmpty() || !confirmPassword.isEmpty()) && !newPassword.equals(confirmPassword)) {
            layoutNewPassword.setError(getString(R.string.matching_password));
            layoutConfirmPassword.setError(getString(R.string.matching_password));
            valide = false;
        }
        return valide;
    }


    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        BtnRestPassword = findViewById(R.id.btn_reinirialiser);
        ConfirmPassword = findViewById(R.id.confirm_password);
        NewPassword = findViewById(R.id.new_password);
        layoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);
        layoutNewPassword = findViewById(R.id.input_layout_new_password);

        BtnBack.setOnClickListener(this);
        BtnRestPassword.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this, LoginActivity.class));
    }

}