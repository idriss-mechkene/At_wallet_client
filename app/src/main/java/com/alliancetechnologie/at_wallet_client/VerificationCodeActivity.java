package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceInterface;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VerificationCodeActivity extends AppCompatActivity implements View.OnClickListener, AuthServiceInterface {

    ImageView BtnBack;
    TextView txtEmail, txtResend;
    OtpTextView CodeVerif;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;
    AuthServiceImpl authServiceImpl;
    ResponseVerification responseVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        authServiceImpl = new AuthServiceImpl(getApplicationContext(), this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            responseVerification = (ResponseVerification) extras.getSerializable("Response");
            txtEmail.setText(responseVerification.getEmail());
            VerifyCode(responseVerification.getVerificationCode());
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
        dialogLoder.dismiss();
        VerifyCode(responseVerification.getVerificationCode());
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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.txt_resend:
                if (responseVerification != null) {
                    dialogLoder = Utils.StartLoader(this);
                    authServiceImpl.ResendVerificationCode(responseVerification.getRequestNumber(), responseVerification.getEmail());
                }
                break;
        }
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        txtResend = findViewById(R.id.txt_resend);
        txtEmail = findViewById(R.id.txt_email);
        CodeVerif = findViewById(R.id.codeVerification);

        BtnBack.setOnClickListener(this);
        txtResend.setOnClickListener(this);
    }


    //code verification
    private void VerifyCode(String correctCode) {
        CodeVerif.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                if (!otp.equals(correctCode)) {
                    // Toast.makeText(VerificationCodeActivity.this, "The OTP is incorrect " + otp, Toast.LENGTH_SHORT).show();
                    CodeVerif.showError();
                } else {
                    // Toast.makeText(VerificationCodeActivity.this, "The OTP is correct " + otp, Toast.LENGTH_SHORT).show();
                    CodeVerif.showSuccess();
                    Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                    intent.putExtra("Email", responseVerification.getEmail());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

}