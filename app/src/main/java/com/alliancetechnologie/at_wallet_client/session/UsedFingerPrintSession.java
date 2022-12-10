package com.alliancetechnologie.at_wallet_client.session;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class UsedFingerPrintSession {
    private final static String SHARED_PREFERENCES_FILE = "local_storage_use_fingerPrint";

    public static void saveIntoSharedPreferences(boolean useFingerPrint, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCES_FILE, useFingerPrint);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_FILE, false);
    }

    public static void cancelSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFERENCES_FILE);
        editor.apply();
    }
}
