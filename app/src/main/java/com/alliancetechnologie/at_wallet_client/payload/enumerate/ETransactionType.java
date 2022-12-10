package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum ETransactionType {

    DEBIT("DEBIT"),
    CREDIT("CREDIT");


    private String name;

    public String getName() {
        return name;
    }

    ETransactionType(String name) {
        this.name = name;
    }
}
