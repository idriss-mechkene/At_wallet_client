package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestUpdatePassword;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.profile.ProfileServiceInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, ProfileServiceInterface {

    ImageView BtnBack;
    TextInputEditText NewPassword, OldPassword, ConfirmPassword;
    TextInputLayout layoutNewPassword, layoutOldPassword, layoutConfirmPassword;
    Button BtnEditPassword;
    CustomerAlertDialog customerAlertDialog;
    RequestUpdatePassword requestUpdatePassword;
    ProfileServiceImpl profileService;
    Dialog dialogLoder;
    String oldPassword, newPassword, confirmPassword;
    boolean firstUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        profileService = new ProfileServiceImpl(getApplicationContext(), this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            firstUpdate = extras.getBoolean("FIRST_LOGIN");
            BtnBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessUpdatePassword() {
        dialogLoder.dismiss();
        if (firstUpdate)
            customerAlertDialog.showSuccessDialog("Mot de Passe modifier avec succès", HomeActivity.class);
        else
            customerAlertDialog.showSuccessDialog("Mot de Passe modifier avec succès", SettingActivity.class);
    }

    @Override
    public void onSuccessUpdateProfile(User user) {

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                if (!firstUpdate)
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                break;
            case R.id.btn_edit_password:
                oldPassword = Objects.requireNonNull(OldPassword.getText()).toString();
                newPassword = Objects.requireNonNull(NewPassword.getText()).toString();
                confirmPassword = Objects.requireNonNull(ConfirmPassword.getText()).toString();
                if (Valider()) {
                    dialogLoder = Utils.StartLoader(this);
                    requestUpdatePassword = new RequestUpdatePassword();
                    requestUpdatePassword.setUserId(RSUserSession.getIdUser(this));
                    requestUpdatePassword.setNewPassword(newPassword);
                    requestUpdatePassword.setLastPassword(oldPassword);
                    requestUpdatePassword.setFocerupdate(false);
                    profileService.updatePassword(RSUserSession.getToken(getApplicationContext()), requestUpdatePassword);
                }
                break;
        }
    }

    private boolean Valider() {
        boolean valide = true;
        if (oldPassword.isEmpty()) {
            layoutOldPassword.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutOldPassword.setError(null);
        }
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
        if (!oldPassword.isEmpty() && oldPassword.length() < 6) {
            layoutOldPassword.setError(getString(R.string.champs_size_password));
            valide = false;
        }
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            layoutNewPassword.setError(getString(R.string.champs_size_password));
            valide = false;
        }
        if (!confirmPassword.isEmpty() && confirmPassword.length() < 6) {
            layoutConfirmPassword.setError(getString(R.string.champs_size_password));
            valide = false;
        }
        if (!confirmPassword.isEmpty() && confirmPassword.length() >= 6 && !confirmPassword.equals(newPassword)) {
            layoutConfirmPassword.setError(getString(R.string.matching_password));
            valide = false;
        }
        return valide;
    }


    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        BtnEditPassword = findViewById(R.id.btn_edit_password);
        OldPassword = findViewById(R.id.oldPassword);
        NewPassword = findViewById(R.id.new_password);
        ConfirmPassword = findViewById(R.id.confirm_password);

        layoutNewPassword = findViewById(R.id.input_layout_new_password);
        layoutOldPassword = findViewById(R.id.input_layout_old_password);
        layoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);

        BtnBack.setOnClickListener(this);
        BtnEditPassword.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!firstUpdate)
            startActivity(new Intent(this, SettingActivity.class));
    }

}