package com.alliancetechnologie.at_wallet_client.payload;

import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;

import lombok.Data;

@Data
public class ResponseAlimentationAccount {

    private String balance;
    private String solde;
    private EOperatorCode operatorCode;
}
