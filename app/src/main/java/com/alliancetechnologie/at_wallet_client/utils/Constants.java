package com.alliancetechnologie.at_wallet_client.utils;

public final class Constants {

    public static final int SPLASH_TIME_OUT = 5000;
    public static final int NUMBER_DIGIT = 3;
    public static final String TOKEN_HEADER = "Authorization";
    public static final String CURRENCY_CODE = "TND";

    private Constants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
