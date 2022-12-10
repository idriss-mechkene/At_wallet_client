package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum EOperatorCode {

    OO("OO"),
    OR("OR"),
    TT("TT"),
    LC("LC");

    private String name;

    public String getName() {
        return name;
    }

    EOperatorCode(String name) {
        this.name = name;
    }

}
