package com.alliancetechnologie.at_wallet_client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdateProfile;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ProfileServiceInterface {

    ImageView BtnBack;
    TextInputEditText Email, PhoneNumber, FirstName, LastName;
    TextInputLayout layoutEmail, layoutPhone, layoutFirstName, layoutLastName;
    Button BtnEdit;
    String phone, email, firstName, lastName;
    ProfileServiceImpl profileServiceImpl;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        initUserData();
        customerAlertDialog = new CustomerAlertDialog(this);
        profileServiceImpl = new ProfileServiceImpl(getApplicationContext(), this);
    }

    @Override
    public void onSuccessUpdatePassword() {

    }

    @Override
    public void onSuccessUpdateProfile(User user) {
        dialogLoder.dismiss();
        RSUserSession.updateIntoSharedPreferences(user, this);
        customerAlertDialog.showSuccessDialog("Update profile avec success", SettingActivity.class);
    }

    @Override
    public void onSuccessAlimenterAccount(ResponseAlimentationAccount responseAlimentationAccount) {

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

    private void initUserData() {
        try {
            User user = RSUserSession.getLocalStorage(this);
            FirstName.setText(user.getFirstName());
            LastName.setText(user.getLastName());
            PhoneNumber.setText(user.getPhoneNumber());
            Email.setText(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
            case R.id.btn_edit_profile:
                email = Email.getText().toString().trim();
                phone = PhoneNumber.getText().toString().trim();
                firstName = FirstName.getText().toString().trim();
                lastName = LastName.getText().toString().trim();
                if (Valider()) {
                    dialogLoder = Utils.StartLoader(this);
                    RequestUpdateProfile requestUpdateProfile = new RequestUpdateProfile();
                    requestUpdateProfile.setAccountId(RSUserSession.getIdAccount(this));
                    requestUpdateProfile.setFirstName(firstName);
                    requestUpdateProfile.setLastName(lastName);
                    requestUpdateProfile.setPhoneNumber(phone.replace(" ", ""));
                    profileServiceImpl.updateProfile(RSUserSession.getToken(this), requestUpdateProfile);
                }
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
        if (phone.isEmpty()) {
            layoutPhone.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutPhone.setError(null);
        }
        if (firstName.isEmpty()) {
            layoutFirstName.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutFirstName.setError(null);
        }
        if (lastName.isEmpty()) {
            layoutLastName.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutLastName.setError(null);
        }
        if (!email.isEmpty() && email.length() != 8 && (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            layoutEmail.setError(getString(R.string.email_invalide));
            valide = false;
        }
//        if (!tel.isEmpty() && (!Patterns.PHONE.matcher(tel).matches())) {
//            layoutTel.setError(getString(R.string.num_tel_invalide));
//            valide = false;
//        }
        return valide;
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        BtnEdit = findViewById(R.id.btn_edit_profile);
        LastName = findViewById(R.id.lastName);
        FirstName = findViewById(R.id.firstName);
        Email = findViewById(R.id.email);
        PhoneNumber = findViewById(R.id.phone);
        layoutLastName = findViewById(R.id.input_layout_lastName);
        layoutFirstName = findViewById(R.id.input_layout_firstName);
        layoutEmail = findViewById(R.id.input_layout_email);
        layoutPhone = findViewById(R.id.input_layout_phone);

        BtnBack.setOnClickListener(this);
        BtnEdit.setOnClickListener(this);

        PhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("TN"));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}