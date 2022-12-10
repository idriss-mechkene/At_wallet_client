package com.alliancetechnologie.at_wallet_client.webservice.service.transaction;

import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.WebService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TransactionService {

    public Observable<List<Transactions>> get5LastTransactionByAccountId(String token, Long accountId) {
        return WebService.getInstance().getApi().get_last5_transactionBy_IdAccount(token, accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Transactions>> getTransactionByAccountId(String token, Long accountId) {
        return WebService.getInstance().getApi().get_transactionBy_IdAccount(token, accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
