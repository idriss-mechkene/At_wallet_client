package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceInterface;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VerificationCodeActivityRegister extends AppCompatActivity implements View.OnClickListener, AuthServiceInterface {

    ImageView BtnBack;
    TextView BtnResend;
    Button BtnVerify;
    OtpTextView CodeVerif;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;
    AuthServiceImpl authServiceImpl;
    ResponseVerification responseVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_register);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        authServiceImpl = new AuthServiceImpl(getApplicationContext(), this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            responseVerification = (ResponseVerification) extras.getSerializable("Response");
        }

        CodeVerif.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
                Log.d("TAG", "onInteractionListener: ");
                BtnVerify.setEnabled(false);
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                Log.d("TAG", "onOTPComplete: ");
                BtnVerify.setEnabled(true);
            }
        });
    }

    @Override
    public void onSuccessLogin(User user) {

    }

    @Override
    public void onSuccessRegistration(ResponseVerification responseVerification) {

    }

    @Override
    public void onCompleteVerifyRegistration() {
        dialogLoder.dismiss();
        customerAlertDialog.showSuccessDialog("Votre compte a été vérifié merci de consulter votre boite mail", LoginActivity.class);
    }

    @Override
    public void onSuccessForgetPassword(ResponseVerification responseVerification) {

    }

    @Override
    public void onSuccessResendCode(ResponseVerification responseVerification) {
        dialogLoder.dismiss();
        this.responseVerification.setVerificationCode(responseVerification.getVerificationCode());
    }

    @Override
    public void onCompleteRestPassword() {

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
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.txt_resend:
                if (responseVerification != null) {
                    dialogLoder = Utils.StartLoader(this);
                    authServiceImpl.ResendVerificationCode(responseVerification.getRequestNumber(), responseVerification.getEmail());
                }
                break;
            case R.id.btn_verify:
                if (responseVerification != null && CodeVerif.getOTP().length() == 4) {
                    dialogLoder = Utils.StartLoader(this);
                    authServiceImpl.VerifyRegistration(responseVerification.getRequestNumber(), CodeVerif.getOTP());
                }
                break;
        }
    }


    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        BtnResend = findViewById(R.id.txt_resend);
        BtnVerify = findViewById(R.id.btn_verify);
        CodeVerif = findViewById(R.id.codeVerification);

        BtnBack.setOnClickListener(this);
        BtnResend.setOnClickListener(this);
        BtnVerify.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }
}