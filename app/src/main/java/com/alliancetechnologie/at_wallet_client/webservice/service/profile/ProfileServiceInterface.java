package com.alliancetechnologie.at_wallet_client.webservice.service.profile;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.ResponseAlimentationAccount;

public interface ProfileServiceInterface {

    void onSuccessUpdatePassword();

    void onSuccessUpdateProfile(User user);

    void onSuccessAlimenterAccount(ResponseAlimentationAccount responseAlimentationAccount);

    void onError(String msg, int code);

}
