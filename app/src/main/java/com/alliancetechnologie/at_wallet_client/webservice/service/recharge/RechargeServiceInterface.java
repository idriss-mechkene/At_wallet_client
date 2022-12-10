package com.alliancetechnologie.at_wallet_client.webservice.service.recharge;

import com.alliancetechnologie.at_wallet_client.entity.Operator;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;

import java.util.List;

public interface RechargeServiceInterface {

    void onSuccessListOperator(List<Operator> operators);

    void onSuccessTopUp(Transactions transactions);

    void onError(String msg, int code);
}
