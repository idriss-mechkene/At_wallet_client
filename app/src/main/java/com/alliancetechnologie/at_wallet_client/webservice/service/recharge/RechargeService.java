package com.alliancetechnologie.at_wallet_client.webservice.service.recharge;

import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.RequestTopUp;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.WebService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RechargeService {

    public Observable<List<Operator>> getOperator(String token) {
        return WebService.getInstance().getApi().get_operator(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Transactions> rechargeTopUp(String token, RequestTopUp requestTopUp) {
        return WebService.getInstance().getApi().recharge_topUp(token,requestTopUp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
