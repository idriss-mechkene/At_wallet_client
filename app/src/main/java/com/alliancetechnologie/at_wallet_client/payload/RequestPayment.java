package com.alliancetechnologie.at_wallet_client.payload;

import com.alliancetechnologie.at_wallet_client.payload.enumerate.EModePaymentQR;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestPayment {

    private Long accountId;
    private String chaine_qr;
    private String refQR;
    private EModePaymentQR eModePaymentQR;
}
