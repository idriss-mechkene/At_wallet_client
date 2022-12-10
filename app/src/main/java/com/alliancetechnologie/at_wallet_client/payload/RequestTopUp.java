package com.alliancetechnologie.at_wallet_client.payload;

import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;

import lombok.Data;

@Data
public class RequestTopUp {

    private Long accountId;
    private EOperatorCode operatorCode;
    private String phoneNumber;
    private String amount;
}
