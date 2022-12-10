package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum ECanalPayment {

    QR_CODE("QR_CODE"),
    RECHARGE("RECHARGE"),
    ALIMENTATION("ALIMENTATION"),
    CARTE("CARTE");

    private String name;

    public String getName() {
        return name;
    }

    ECanalPayment(String name) {
        this.name = name;
    }
}
