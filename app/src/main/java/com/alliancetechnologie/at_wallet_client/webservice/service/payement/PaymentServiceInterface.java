package com.alliancetechnologie.at_wallet_client.webservice.service.payement;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.ResponseDetailsQR;

public interface PaymentServiceInterface {

    void onSuccessPayment(Transactions transactions);

    void onSuccessDetailQR(ResponseDetailsQR responseDetailsQR);

    void onError(String msg, int code);
}
