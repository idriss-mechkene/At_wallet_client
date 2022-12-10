package com.alliancetechnologie.at_wallet_client.payload;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseVerification implements Serializable {

    private Long requestNumber;
    private String verificationCode;
    private String email;
}
