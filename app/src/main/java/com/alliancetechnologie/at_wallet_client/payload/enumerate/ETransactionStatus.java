package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum ETransactionStatus {

    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    PENDING("PENDING");


    private String name;

    public String getName() {
        return name;
    }

    ETransactionStatus(String name) {
        this.name = name;
    }
}
