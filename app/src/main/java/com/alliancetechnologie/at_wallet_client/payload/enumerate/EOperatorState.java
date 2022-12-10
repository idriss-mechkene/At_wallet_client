package com.alliancetechnologie.at_wallet_client.payload.enumerate;

public enum EOperatorState {

    ENABLED("ENABLED"),
    DISABLED("DISABLED");

    private String name;

    public String getName() {
        return name;
    }

    EOperatorState(String name) {
        this.name = name;
    }
}
