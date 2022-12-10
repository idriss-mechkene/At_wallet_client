package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum EModePaymentQR {

    QR("QR"),
    REF("REF");

    private String name;

    public String getName() {
        return name;
    }

    EModePaymentQR(String name) {
        this.name = name;
    }
}
