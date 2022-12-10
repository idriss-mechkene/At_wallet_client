package com.alliancetechnologie.at_wallet_client.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alliancetechnologie.at_wallet_client.HomeActivity;
import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ECanalPayment;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionType;
import com.alliancetechnologie.at_wallet_client.session.UsedFingerPrintSession;

public class CustomerAlertDialog {

    private final Context context;

    public CustomerAlertDialog(Context context) {
        this.context = context;
    }

    public void showSuccessDialog(String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_ok);
        TextView text = dialog.findViewById(R.id.message);
        text.setText(message);
        Button dialogButton = dialog.findViewById(R.id.btnOk);
        dialogButton.setOnClickListener(v -> {
            dialog.dismiss();
//            context.startActivity(new Intent(context, MainActivity.class));
        });
//        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void showSuccessDialog(String message, Class classe) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_ok);
        TextView text = dialog.findViewById(R.id.message);
        text.setText(message);
        Button dialogButton = dialog.findViewById(R.id.btnOk);
        dialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            context.startActivity(new Intent(context, classe));
        });
//        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void showErrorDialog(String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialg_error);

        TextView text = dialog.findViewById(R.id.message);
        text.setText(message);
        Button dialogButton = dialog.findViewById(R.id.btnOk);
        dialogButton.setOnClickListener(v -> {
            dialog.dismiss();
//            context.startActivity(new Intent(context, MainActivity.class));
        });
        dialog.show();
    }

    public void showFingerPrintDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_fingerprint);

        dialog.findViewById(R.id.btn_yes).setOnClickListener(v -> {
            dialog.dismiss();
            UsedFingerPrintSession.saveIntoSharedPreferences(true, context);
            context.startActivity(new Intent(context, HomeActivity.class));
        });
        dialog.findViewById(R.id.btn_no).setOnClickListener(v -> {
            dialog.dismiss();
            context.startActivity(new Intent(context, HomeActivity.class));
        });
        dialog.show();
    }

    @SuppressLint({"DefaultLocale"})
    public void showDetailTransactionDialog(Transactions transactions, Context context) {
        if (transactions != null) {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.transaction_detail);

            LinearLayout AuthorisationLayout = dialog.findViewById(R.id.layout_autorisation);
            TextView Authorisation = dialog.findViewById(R.id.txt_num_autorisation);
            TextView Type = dialog.findViewById(R.id.text_type);
            TextView Date = dialog.findViewById(R.id.txt_date);
            TextView Amount = dialog.findViewById(R.id.txt_amount);
            TextView Prestataire = dialog.findViewById(R.id.text_prestataire);
            TextView NumTransaction = dialog.findViewById(R.id.txt_num_transaction);

            if (transactions.getAuthorisationCode() != null) {
                AuthorisationLayout.setVisibility(View.VISIBLE);
                if (transactions.getPaymentCanal().equals(ECanalPayment.QR_CODE)) {
                    AuthorisationLayout.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                } else {
                    AuthorisationLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                Authorisation.setText(transactions.getAuthorisationCode());
            } else {
                AuthorisationLayout.setVisibility(View.GONE);
            }
            Type.setText(transactions.getTransactionType().getName());
            Date.setText(transactions.getTransactionDate());
            NumTransaction.setText(String.format("%s", transactions.getTransactionNumber()));
            Amount.setText(Utils.showFormattedAmount(transactions.getTransactionAmount()));

            if (transactions.getOperatorCode() != null) {
                Prestataire.setText(transactions.getOperatorName());
                switch (transactions.getOperatorCode()) {
                    case OR:
                        Prestataire.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_orange, 0);
                        break;
                    case OO:
                        Prestataire.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ooredoo, 0);
                        break;
                    case TT:
                        Prestataire.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_logo_tt, 0);
                        break;
                    case LC:
                        Prestataire.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lyca_mobile, 0);
                        break;
                }
            } else {
                Prestataire.setText(transactions.getPrestataireName());
            }
            dialog.show();
        }
    }
}
