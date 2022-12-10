package com.alliancetechnologie.at_wallet_client.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import androidx.biometric.BiometricManager;

import com.alliancetechnologie.at_wallet_client.HistoryActivity;
import com.alliancetechnologie.at_wallet_client.HomeActivity;
import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.SettingActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    @SuppressLint("NonConstantResourceId")
    public static boolean NavigationMenus(Activity activity, MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                break;
            case R.id.history:
                intent = new Intent(activity.getApplicationContext(), HistoryActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                break;
            case R.id.setting:
                intent = new Intent(activity.getApplicationContext(), SettingActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                break;
        }
        return true;
    }

    public static String showFormattedAmount(String amount) {
        if (isNumeric(amount)) {
            try {
                String FormatedAmount;
                String part1;
                String part2;
                FormatedAmount = leftPad(amount, Constants.NUMBER_DIGIT + 1);
                part1 = FormatedAmount.substring(0, FormatedAmount.length() - Constants.NUMBER_DIGIT);

                part2 = FormatedAmount.substring(FormatedAmount.length() - Constants.NUMBER_DIGIT);
                part1 = amountSpacing(part1);

                FormatedAmount = part1 + "." + part2 + " " + Constants.CURRENCY_CODE;
                return FormatedAmount;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String amountSpacing(String ch) {
        if (ch.length() > Constants.NUMBER_DIGIT) {
            return amountSpacing(ch.substring(0, ch.length() - Constants.NUMBER_DIGIT)) + " "
                    + ch.substring(ch.length() - Constants.NUMBER_DIGIT);
        }
        return ch;
    }

    public static String leftPad(String str, int size) {
        StringBuilder padded = new StringBuilder(str == null ? "" : str);
        while (padded.length() < size) {
            padded.insert(0, '0');
        }
        return padded.toString();
    }

    public static String showFormattedAmount2(String Amount) {
        try {
            String FormatedAmount;
            long amount = Long.parseLong(Amount);
            String part2 = String.valueOf((amount % 1000));
            String part1 = String.valueOf(amount / 1000);
            Log.e("TAG", "temp=: " + part1);
            if (part1.length() > 3) {
                int i = part1.length() - 3;
                while (i >= 0) {
                    part1 = part1.substring(0, i) + ' ' + part1.substring(i);
                    Log.e("TAG", "temp=: " + part1);
                    i = i - 3;
                }
            }
            FormatedAmount = part1 + "." + leftPad(part2, Constants.NUMBER_DIGIT) + " " + Constants.CURRENCY_CODE;
            Log.e("TAG", "FromatedAmount: " + FormatedAmount);
            return FormatedAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String showFormattedAmount3(String amount) {
        try {

            amount = NumberFormat.getCurrencyInstance(Locale.US).format(Double.valueOf(amount));
            amount=amount.substring(0,amount.length()-3);
            amount = amount.replace('$',' ');
            amount = amount.replaceFirst(","," ");
            amount = amount.replaceFirst(",",".");

            return amount+" TND";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static Dialog StartLoader(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public void StopLoader(Context context) {
        StartLoader(context).dismiss();
    }

    public static boolean isFingerPrintExist(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                return true;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                Log.e("MY_APP_TAG", "Prompts the user to create credentials that your app accepts.");
                return false;
        }
        return false;
    }
}
