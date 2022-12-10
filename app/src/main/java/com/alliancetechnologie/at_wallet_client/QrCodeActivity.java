package com.alliancetechnologie.at_wallet_client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestPayment;
import com.alliancetechnologie.at_wallet_client.payload.ResponseDetailsQR;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.EModePaymentQR;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.payement.PaymentServiceImp;
import com.alliancetechnologie.at_wallet_client.webservice.service.payement.PaymentServiceInterface;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;

import java.util.Collections;
import java.util.Objects;

public class QrCodeActivity extends AppCompatActivity implements View.OnClickListener, PaymentServiceInterface {

    TextInputEditText inputQRCode;
    TextInputLayout layoutQRCode;
    ImageView BtnBack;
    ImageButton ImgScanner;
    Button btnValidate;
    CodeScannerView scannerView;
    CodeScanner mCodeScanner;
    PaymentServiceImp paymentServiceImp;
    CustomerAlertDialog customerAlertDialog;
    Dialog dialogLoder;
    String RefQrCode;
    RequestPayment requestPayment ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        init();

        customerAlertDialog = new CustomerAlertDialog(this);
        paymentServiceImp = new PaymentServiceImp(getApplicationContext(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    @Override
    public void onSuccessPayment(Transactions transactions) {
        dialogLoder.dismiss();
//        hideCodeScanner();
        RSUserSession.updateBalanceIntoSharedPreferences(transactions.getBalanceAfter(), getApplicationContext());
        customerAlertDialog.showSuccessDialog("Paiement avec success", HomeActivity.class);
    }

    @Override
    public void onSuccessDetailQR(ResponseDetailsQR responseDetailsQR) {
        Log.e("TAG", "onSuccessDetailQR: " + responseDetailsQR.toString());
        dialogLoder.dismiss();
        hideCodeScanner();
        //DilogDetails
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_detail_qr);

        TextView Reference = dialog.findViewById(R.id.txt_ref);
        TextView Provider = dialog.findViewById(R.id.text_prestataire);
        TextView City = dialog.findViewById(R.id.text_ville);
        TextView Amount = dialog.findViewById(R.id.txt_amount);
        Button btnCancel = dialog.findViewById(R.id.btnAnnuler);
        Button btnValidate = dialog.findViewById(R.id.btnConfirm);

        Reference.setText(responseDetailsQR.getRefpaiement());
        Provider.setText(responseDetailsQR.getPrestataire());
        City.setText(responseDetailsQR.getVille());
        Amount.setText(Utils.showFormattedAmount(responseDetailsQR.getMontant()));

        btnValidate.setOnClickListener(v -> {
            dialog.dismiss();
            dialogLoder = Utils.StartLoader(this);
            paymentServiceImp.PaymentQr(RSUserSession.getToken(getApplicationContext()), requestPayment);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
        //customerAlertDialog.showDetailQRDialog(responseDetailsQR, this);
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
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
            case R.id.btn_validate:
                RefQrCode = Objects.requireNonNull(inputQRCode.getText()).toString().trim();
                if (Valider()) {
                    dialogLoder = Utils.StartLoader(this);
                    requestPayment=new RequestPayment();
                    requestPayment.setAccountId(RSUserSession.getIdAccount(getApplicationContext()));
                    requestPayment.setRefQR(RefQrCode);
                    requestPayment.setEModePaymentQR(EModePaymentQR.REF);
                    paymentServiceImp.detailsQR(RSUserSession.getToken(getApplicationContext()), requestPayment);
                    // startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
                break;
            case R.id.img_scanner:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 123);
                } else {
                    Log.e("TAG", "onClick: ok");
                    startScanning();
                }
        }
    }

    private boolean Valider() {
        boolean valide = true;
        if (RefQrCode.isEmpty()) {
            layoutQRCode.setError(getString(R.string.champs_obligatoir));
            valide = false;
        } else {
            layoutQRCode.setError(null);
        }
        return valide;
    }

    private void hideCodeScanner() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
            mCodeScanner.startPreview();
            scannerView.setVisibility(View.GONE);
        }
    }

    private void startScanning() {
        scannerView.setVisibility(View.VISIBLE);
        if (mCodeScanner == null) {
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setScanMode(ScanMode.SINGLE);
            mCodeScanner.setAutoFocusEnabled(true);
            mCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        }
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            mCodeScanner.releaseResources();
            mCodeScanner.startPreview();
            scannerView.setVisibility(View.GONE);
            try {
                Log.e("TAG", "decoded qr= " + result.getText());
                dialogLoder = Utils.StartLoader(this);
                requestPayment=new RequestPayment();
                requestPayment.setAccountId(RSUserSession.getIdAccount(getApplicationContext()));
                requestPayment.setChaine_qr(result.getText());
                requestPayment.setEModePaymentQR(EModePaymentQR.QR);
                paymentServiceImp.detailsQR(RSUserSession.getToken(getApplicationContext()), requestPayment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        mCodeScanner.setErrorCallback(error -> runOnUiThread(() -> {
            Log.e("TAG", "run: error" + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }));
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        ImgScanner = findViewById(R.id.img_scanner);
        btnValidate = findViewById(R.id.btn_validate);
        scannerView = findViewById(R.id.scanner_view);
        inputQRCode = findViewById(R.id.code);
        layoutQRCode = findViewById(R.id.input_layout_code);

        BtnBack.setOnClickListener(this);
        ImgScanner.setOnClickListener(this);
        btnValidate.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mCodeScanner != null && mCodeScanner.isPreviewActive()) {
            mCodeScanner.releaseResources();
            mCodeScanner.startPreview();
            scannerView.setVisibility(View.GONE);
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}