package com.alliancetechnologie.at_wallet_client;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestTopUp;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;
import com.alliancetechnologie.at_wallet_client.session.RSUserSession;
import com.alliancetechnologie.at_wallet_client.utils.CustomerAlertDialog;
import com.alliancetechnologie.at_wallet_client.utils.Utils;
import com.alliancetechnologie.at_wallet_client.webservice.service.recharge.RechargeServiceImpl;
import com.alliancetechnologie.at_wallet_client.webservice.service.recharge.RechargeServiceInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener, RechargeServiceInterface {

    ImageView BtnBack;
    MaterialButtonToggleGroup toggleButtonOperator;
    TextInputLayout layoutAmount, layoutPhone;
    TextInputEditText AmountRecharge, PhoneNumber;
    Button btnRecharge;
    TextView txt_Limte;
    String phone, amount;
    EOperatorCode eOperatorCode;
    Map<EOperatorCode, Operator> operators;
    RechargeServiceImpl rechargeService;
    Dialog dialogLoder;
    CustomerAlertDialog customerAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        init();

        initOperators();

        rechargeService = new RechargeServiceImpl(this, this);
        customerAlertDialog = new CustomerAlertDialog(this);

        PhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher("TN") {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
//                Log.e("TAG", "afterTextChanged: ");
                if (s.length() == 1) {
                    layoutPhone.setError(null);
                    setIndicator(s.toString());
                } else if (s.length() == 0) {
                    layoutPhone.setError(null);
                    toggleButtonOperator.clearChecked();
                } else if (eOperatorCode != null) {
                    layoutPhone.setError(null);
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initOperators() {
        operators = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Type type = new TypeToken<List<Operator>>() {
            }.getType();
            List<Operator> listOperators = new Gson().fromJson(getIntent().getStringExtra("OperatorsList"), type);
            if (listOperators.size() > 0) {
                Log.e("tag", "initOperators: " + listOperators);
                for (Operator op : listOperators) {
                    operators.put(op.getOperatorCode(), op);
                    MaterialButton button =
                            (MaterialButton) getLayoutInflater().inflate(R.layout.single_button_operator, toggleButtonOperator, false);

                    if (op.getOperatorCode().equals(EOperatorCode.OO)) {
                        button.setIcon(getDrawable(R.drawable.ic_ooredoo));
                        button.setId(op.getOperatorCode().hashCode());
                        toggleButtonOperator.addView(button);
                    } else if (op.getOperatorCode().equals(EOperatorCode.TT)) {
                        button.setIcon(getDrawable(R.drawable.ic_logo_tt));
                        button.setId(op.getOperatorCode().hashCode());
                        toggleButtonOperator.addView(button);
                    } else if (op.getOperatorCode().equals(EOperatorCode.OR)) {
                        button.setIcon(getDrawable(R.drawable.ic_orange));
                        button.setId(op.getOperatorCode().hashCode());
                        toggleButtonOperator.addView(button);
                    } else if (op.getOperatorCode().equals(EOperatorCode.LC)) {
                        button.setIcon(getDrawable(R.drawable.ic_lyca_mobile));
                        button.setId(op.getOperatorCode().hashCode());
                        toggleButtonOperator.addView(button);
                    }
                }
            }
        }
    }

    @Override
    public void onSuccessListOperator(List<Operator> operators) {

    }

    @Override
    public void onSuccessTopUp(Transactions transactions) {
        dialogLoder.dismiss();
        RSUserSession.updateBalanceIntoSharedPreferences(transactions.getBalanceAfter(), this);
        customerAlertDialog.showSuccessDialog("Votre recharge de " + Utils.showFormattedAmount(transactions.getTransactionAmount()) +
                " a été effectuer avec succès", HomeActivity.class);
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
            case R.id.btn_recharge:
                phone = Objects.requireNonNull(PhoneNumber.getText()).toString();
                amount = Objects.requireNonNull(AmountRecharge.getText()).toString();
                if (Validate()) {
                    Log.e("TAG", "onClick: " + amount);
                    confirmationRecharge();
                }
                break;
        }
    }

    private void confirmationRecharge() {
        //Dilog Confirmation Recharge
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_detail_recharge);

        TextView Amount = dialog.findViewById(R.id.txt_amount);
        TextView phoneNumber = dialog.findViewById(R.id.txt_telephone);
        ImageView Provider = dialog.findViewById(R.id.img_operateur);
        Button btnCancel = dialog.findViewById(R.id.btnAnnuler);
        Button btnValidate = dialog.findViewById(R.id.btnConfirm);

        phoneNumber.setText(phone);
        Amount.setText(Utils.showFormattedAmount(amount));

        if (toggleButtonOperator.getCheckedButtonId() == EOperatorCode.TT.hashCode()) {
            Provider.setImageResource(R.drawable.ic_logo_tt);
        } else if (toggleButtonOperator.getCheckedButtonId() == EOperatorCode.OO.hashCode()) {
            Provider.setImageResource(R.drawable.ic_ooredoo);
        } else if (toggleButtonOperator.getCheckedButtonId() == EOperatorCode.OR.hashCode()) {
            Provider.setImageResource(R.drawable.ic_orange);
        } else if (toggleButtonOperator.getCheckedButtonId() == EOperatorCode.LC.hashCode()) {
            Provider.setImageResource(R.drawable.ic_lyca_mobile);
        }

        btnValidate.setOnClickListener(v -> {
            dialog.dismiss();
            dialogLoder = Utils.StartLoader(this);
            RequestTopUp requestTopUp = new RequestTopUp();
            requestTopUp.setAccountId(RSUserSession.getIdAccount(this));
            requestTopUp.setOperatorCode(eOperatorCode);
            requestTopUp.setAmount(amount);
            requestTopUp.setPhoneNumber(phone.replace(" ", ""));
            rechargeService.RechargeTopUp(RSUserSession.getToken(this), requestTopUp);
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private boolean Validate() {
        boolean valid = true;
        if (!amount.isEmpty() && eOperatorCode != null) {
            Log.e("TAG", "Validate: ");
            try {
                long min = Long.parseLong(Objects.requireNonNull(operators.get(eOperatorCode)).getOperatorMin());
                long max = Long.parseLong(Objects.requireNonNull(operators.get(eOperatorCode)).getOperatorMax());
                long step = Long.parseLong(Objects.requireNonNull(operators.get(eOperatorCode)).getOperatorPas());
                if (Long.parseLong(amount) > max || Long.parseLong(amount) < min) {
                    Log.e("TAG", "Validate: min max");
                    layoutAmount.setError("Montant doit être entre : " + min + " et " + max);
                    valid = false;
                } else if (Long.parseLong(amount) != min && Long.parseLong(amount) != max && (((Long.parseLong(amount) - min) % step) != 0)) {
                    Log.e("TAG", "Validate: step");
                    layoutAmount.setError("Pas doit être par :" + step);
                    valid = false;
                } else {
                    layoutAmount.setError(null);
                }
            } catch (NumberFormatException ne) {
                layoutAmount.setError(getString(R.string.amount_mauvais_format));
                valid = false;
            } catch (Exception e) {
                Log.e("TAG", "Validate: " + e.getMessage());
                valid = false;
            }
        }
        if (amount.isEmpty()) {
            layoutAmount.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (phone.isEmpty()) {
            layoutPhone.setError(getString(R.string.champs_obligatoir));
            valid = false;
        }
        if (toggleButtonOperator.getCheckedButtonId() == View.NO_ID) {
            valid = false;
            Toast.makeText(this, "merci de selectionné l'operateur", Toast.LENGTH_LONG).show();
        }
        if (!phone.isEmpty() && phone.length() != 10) {
            layoutPhone.setError(getString(R.string.num_tel_invalide));
            valid = false;
        }
        return valid;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        Log.e("TAG", "onButtonChecked: " + group.getCheckedButtonId());
        if (group.getCheckedButtonId() == EOperatorCode.OO.hashCode()) {
            eOperatorCode = EOperatorCode.OO;
            layoutAmount.setError(null);
            if (!operators.isEmpty())
                txt_Limte.setText(String.format("Limite entre %s et %s",
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.OO)).getOperatorMin()),
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.OO)).getOperatorMax())));
        } else if (group.getCheckedButtonId() == EOperatorCode.TT.hashCode()) {
            eOperatorCode = EOperatorCode.TT;
            layoutAmount.setError(null);
            if (!operators.isEmpty())
                txt_Limte.setText(String.format("Limite entre %s et %s",
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.TT)).getOperatorMin()),
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.TT)).getOperatorMax())));
        } else if (group.getCheckedButtonId() == EOperatorCode.OR.hashCode()) {
            eOperatorCode = EOperatorCode.OR;
            layoutAmount.setError(null);
            if (!operators.isEmpty())
                txt_Limte.setText(String.format("Limite entre %s et %s",
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.OR)).getOperatorMin()),
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.OR)).getOperatorMax())));
        } else if (group.getCheckedButtonId() == EOperatorCode.LC.hashCode()) {
            eOperatorCode = EOperatorCode.LC;
            layoutAmount.setError(null);
            if (!operators.isEmpty())
                txt_Limte.setText(String.format("Limite entre %s et %s",
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.LC)).getOperatorMin()),
                        Utils.showFormattedAmount(Objects.requireNonNull(operators.get(EOperatorCode.LC)).getOperatorMax())));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setIndicator(String value) {
        switch (value) {
            case "2":
                eOperatorCode = EOperatorCode.OO;
                toggleButtonOperator.check(EOperatorCode.OO.hashCode());
                break;
            case "5":
            case "3":
                eOperatorCode = EOperatorCode.OR;
                toggleButtonOperator.check(EOperatorCode.OR.hashCode());
                break;
            case "4":
            case "9":
            case "7":
                eOperatorCode = EOperatorCode.TT;
                toggleButtonOperator.check(EOperatorCode.TT.hashCode());
                break;
            default:
                eOperatorCode = null;
                toggleButtonOperator.clearChecked();
                layoutPhone.setError(getString(R.string.indicateur_invalide));
                break;
        }
    }

    private void init() {
        BtnBack = findViewById(R.id.backBtn);
        toggleButtonOperator = findViewById(R.id.toggleOperator);
        layoutPhone = findViewById(R.id.input_layout_phone);
        layoutAmount = findViewById(R.id.layout_amount_recharge);
        AmountRecharge = findViewById(R.id.amount_recharge);
        PhoneNumber = findViewById(R.id.phone);
        btnRecharge = findViewById(R.id.btn_recharge);
        txt_Limte = findViewById(R.id.txt_recharge_limit);
        BtnBack.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        toggleButtonOperator.addOnButtonCheckedListener(this);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }

}