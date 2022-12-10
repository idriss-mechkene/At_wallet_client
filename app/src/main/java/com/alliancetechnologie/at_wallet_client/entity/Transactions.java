package com.alliancetechnologie.at_wallet_client.entity;

import com.alliancetechnologie.at_wallet_client.payload.enumerate.ECanalPayment;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionStatus;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionType;

import java.io.Serializable;

import lombok.Data;

@Data
public class Transactions implements Serializable {

    private Long transactionId;
    private String accountNumber;
    private String transactionName;
    private String transactionDate;
    private String prestataireName;
    private ECanalPayment paymentCanal;
    private ETransactionStatus transactionStatus;
    private ETransactionType transactionType;
    private String transactionAmount;
    private String transactionNumber;
    private String balanceAfter;
    private String balanceBefore;
    private String authorisationCode;
    private EOperatorCode operatorCode;
    private String operatorName;
}
