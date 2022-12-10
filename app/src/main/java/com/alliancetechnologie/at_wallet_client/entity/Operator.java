package com.alliancetechnologie.at_wallet_client.entity;

import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorCode;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.EOperatorState;

import java.io.Serializable;

import lombok.Data;

@Data
public class Operator implements Serializable {

    private EOperatorCode operatorCode;
    private String operatorPas;
    private String operatorMax;
    private String operatorMin;
    private EOperatorState operatorState;
}
