package com.alliancetechnologie.at_wallet_client.session;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.google.gson.Gson;

public class RSUserSession {
    private final static String SHARED_PREFERENCES_FILE = "local_storage_user";


    //save user data  json format
    public static void saveIntoSharedPreferences(Object Data, Context context) {
        User userInfo = new Gson().fromJson(new Gson().toJson(Data), User.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_FILE, new Gson().toJson(userInfo));
        editor.apply();
    }

    //get informations from localstorage return user object
    public static User getLocalStorage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        String localStorage = sharedPreferences.getString(SHARED_PREFERENCES_FILE, null);
        return new Gson().fromJson(localStorage, User.class);
    }

    //update informations in localStorage
    public static void updateIntoSharedPreferences(User user, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        User userInfo = getLocalStorage(context);
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        editor.putString(SHARED_PREFERENCES_FILE, new Gson().toJson(userInfo));
        editor.apply();
    }

    public static void updateBalanceIntoSharedPreferences(String amount, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        User userInfo = getLocalStorage(context);
        userInfo.setAccountBalance(amount);
        editor.putString(SHARED_PREFERENCES_FILE, new Gson().toJson(userInfo));
        editor.apply();
    }

    public static String getToken(Context context) {
        Log.e("TAG", "getToken: " + getLocalStorage(context).getToken_type() + getLocalStorage(context).getAccess_token());
        return getLocalStorage(context).getToken_type() + getLocalStorage(context).getAccess_token();
    }

    public static Long getIdUser(Context context) {
        return getLocalStorage(context).getUserId();
    }

    public static Long getIdAccount(Context context) {
        Log.e("TAG", "getIdAccount: " + getLocalStorage(context).getAccountId());
        return getLocalStorage(context).getAccountId();
    }

    public static void cancelSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFERENCES_FILE);
        editor.apply();
    }
}
