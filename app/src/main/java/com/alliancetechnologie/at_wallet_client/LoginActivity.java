package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestLogin;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.session.UsedFingerPrintSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AuthServiceInterface {

    TextInputEditText Email, Password;
    TextInputLayout layoutEmail, layoutPassword;
    RelativeLayout relativeLayoutFingerPrint;
    Button LoginBtn;
    ImageView FingerPrintImg;
    TextView RegisterTxt, ForgetPasswordTxt, infoText;
    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    String email, password;
    AuthServiceImpl authServiceImpl;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initFingerprintAuth();

        customerAlertDialog = new CustomerAlertDialog(this);
        authServiceImpl = new AuthServiceImpl(getApplicationContext(), this);
    }

    @Override
    public void onSuccessLogin(User user) {
        dialogLoder.dismiss();
        RSUserSession.saveIntoSharedPreferences(user, this);
        if (Utils.isFingerPrintExist(this) && !UsedFingerPrintSession.isLoggedIn(this)) {
            customerAlertDialog.showFingerPrintDialog(this);
        } else {
            if (user.isUser_pwd_forced_update()) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                i.putExtra("FIRST_LOGIN", true);
                startActivity(i);
            } else {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }
    }

    @Override
    public void onSuccessRegistration(ResponseVerification responseVerification) {

    }

    @Override
    public void onCompleteVerifyRegistration() {

    }

    @Override
    public void onSuccessForgetPassword(ResponseVerification responseVerification) {
        dialogLoder.dismiss();
        responseVerification.setEmail(email);
        Intent i = new Intent(getApplicationContext(), VerificationCodeActivity.class);
        i.putExtra("Response", responseVerification);
        startActivity(i);
    }

    @Override
    public void onSuccessResendCode(ResponseVerification responseVerification) {

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
            case R.id.txt_regiter:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.btn_login:
                email = Objects.requireNonNull(Email.getText()).toString().trim();
                password = Objects.requireNonNull(Password.getText()).toString().trim();
                if (Valider()) {
                    dialogLoder = Utils.StartLoader(this);
                    RequestLogin requestLogin = new RequestLogin();
                    requestLogin.setEmail(email);
                    requestLogin.setPassword(password);
                    authServiceImpl.Login(requestLogin);
                }
                break;
            case R.id.prob_conn:
                email = Objects.requireNonNull(Email.getText()).toString().trim();
                if (ValidatForgetPassword()) {
                    dialogLoder = Utils.StartLoader(this);
                    authServiceImpl.ForgetPassword(email);
                }
                break;
            case R.id.img_fingerprint:
                launchFingerprintAuth();
                break;
        }
    }

    private boolean Valider() {
        boolean valide = true;
        if (email.isEmpty()) {
            layoutEmail.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutEmail.setError(null);
        }
        if (!email.isEmpty() && (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            layoutEmail.setError(getString(R.string.email_invalide));
            valide = false;
        }
        if (password.isEmpty()) {
            layoutPassword.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutPassword.setError(null);
        }
        if (!password.isEmpty() && password.length() < 6) {
            layoutPassword.setError(getString(R.string.champs_size_password));
            valide = false;
        }
        return valide;
    }

    private boolean ValidatForgetPassword() {
        boolean valide = true;
        if (email.isEmpty()) {
            layoutEmail.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutEmail.setError(null);
        }
        if (!email.isEmpty() && (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            layoutEmail.setError(getString(R.string.email_invalide));
            valide = false;
        }
        return valide;
    }

    private void init() {
        LoginBtn = findViewById(R.id.btn_login);
        RegisterTxt = findViewById(R.id.txt_regiter);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        layoutEmail = findViewById(R.id.input_layout_email);
        layoutPassword = findViewById(R.id.input_layout_password);
        ForgetPasswordTxt = findViewById(R.id.prob_conn);
        relativeLayoutFingerPrint = findViewById(R.id.relative_finger_print);
        FingerPrintImg = findViewById(R.id.img_fingerprint);
        infoText = findViewById(R.id.txt_info);

        LoginBtn.setOnClickListener(this);
        RegisterTxt.setOnClickListener(this);
        ForgetPasswordTxt.setOnClickListener(this);
        FingerPrintImg.setOnClickListener(this);

    }

    private void initFingerprintAuth() {
        // init biometric
        if (UsedFingerPrintSession.isLoggedIn(this) && Utils.isFingerPrintExist(this)) {
            relativeLayoutFingerPrint.setVisibility(View.VISIBLE);
            executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e("TAG", "onAuthenticationError: code:" + errorCode + " message" + errString);
                    infoText.setVisibility(View.VISIBLE);
                    infoText.setText(String.format("Auth error : %s", errString));
//                    Toast.makeText(LoginActivity.this, "Auth Error : " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.d("TAG", "onAuthenticationSucceeded: ");
                    //Toast.makeText(LoginActivity.this, "Auth Succeed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.e("TAG", "onAuthenticationFailed: ");
                    // txtInfo.setText("Auth failed  ");
//                    Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            relativeLayoutFingerPrint.setVisibility(View.GONE);
        }
    }

    private void launchFingerprintAuth() {
        // auth dialog setup
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.confirm_finger_print))
                .setNegativeButtonText(getString(R.string.annuler))
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onBackPressed() {
    }
}