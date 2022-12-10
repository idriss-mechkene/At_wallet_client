package com.alliancetechnologie.at_wallet_client.webservice.service.payement;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestPayment;
import com.alliancetechnologie.at_wallet_client.payload.ResponseDetailsQR;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.WebService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PaymentService {

    public Single<Transactions> payment(String token, RequestPayment requestPayment) {
        return WebService.getInstance().getApi().payment_QR(token, requestPayment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseDetailsQR> payment_detail_QR(String token, RequestPayment requestPayment) {
        return WebService.getInstance().getApi().payment_detail_QR(token, requestPayment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
