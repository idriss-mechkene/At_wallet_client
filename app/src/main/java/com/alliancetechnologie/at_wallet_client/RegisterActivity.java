package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestInscription;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.auth.AuthServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AuthServiceInterface {

    TextView ConnexionTxt;
    Button BtnInscription;
    TextInputEditText Email, Cin, PhoneNumber, FirstName, LastName;
    TextInputLayout layoutFirstName, layoutLastName, layoutEmail, layoutCin, layoutPhoneNumber;
    String phone, firstName, lastName, cin, email;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;
    AuthServiceImpl authServiceImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        authServiceImpl = new AuthServiceImpl(getApplicationContext(), this);

        PhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("TN"));
    }

    @Override
    public void onSuccessLogin(User user) {

    }

    @Override
    public void onSuccessRegistration(ResponseVerification responseVerification) {
        dialogLoder.dismiss();
        responseVerification.setEmail(email);
        Intent intent = new Intent(getApplicationContext(), VerificationCodeActivityRegister.class);
        intent.putExtra("Response", responseVerification);
        startActivity(intent);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.btn_inscription:
                email = Objects.requireNonNull(Email.getText()).toString().trim();
                phone = Objects.requireNonNull(PhoneNumber.getText()).toString();
                firstName = Objects.requireNonNull(LastName.getText()).toString();
                lastName = Objects.requireNonNull(FirstName.getText()).toString();
                cin = Objects.requireNonNull(Cin.getText()).toString();
                if (Validate()) {
                    dialogLoder = Utils.StartLoader(this);
                    RequestInscription requestInscription = new RequestInscription();
                    requestInscription.setFirstName(firstName);
                    requestInscription.setLastName(lastName);
                    requestInscription.setEmail(email);
                    requestInscription.setIdentityCard(cin);
                    requestInscription.setPhoneNumber(phone.replace(" ", ""));
                    authServiceImpl.Registration(requestInscription);
                }
                break;
        }
    }

    private boolean Validate() {
        boolean valid = true;

        if (phone.isEmpty()) {
            layoutPhoneNumber.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (firstName.isEmpty()) {
            layoutFirstName.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (lastName.isEmpty()) {
            layoutLastName.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (email.isEmpty()) {
            layoutEmail.setError(getString(R.string.champs_obligatoir));
            valid = false;
        } else {
            layoutEmail.setError(null);
        }
        if (!email.isEmpty() && (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            layoutEmail.setError(getString(R.string.email_invalide));
            valid = false;
        }
        if (cin.isEmpty()) {
            layoutCin.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (!cin.isEmpty() && cin.length() < 8) {
            layoutCin.setError(getString(R.string.champs_size_cin));
            valid = false;
        }
        return valid;
    }


    private void init() {
        layoutPhoneNumber = findViewById(R.id.input_layout_phone);
        layoutFirstName = findViewById(R.id.input_layout_firstName);
        layoutLastName = findViewById(R.id.input_layout_lastName);
        layoutCin = findViewById(R.id.input_layout_cin);
        layoutEmail = findViewById(R.id.input_layout_email);

        LastName = findViewById(R.id.lastName);
        FirstName = findViewById(R.id.firstName);
        PhoneNumber = findViewById(R.id.phone);
        Email = findViewById(R.id.email);
        Cin = findViewById(R.id.cin);

        ConnexionTxt = findViewById(R.id.txt_login);
        BtnInscription = findViewById(R.id.btn_inscription);

        ConnexionTxt.setOnClickListener(this);
        BtnInscription.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }

}