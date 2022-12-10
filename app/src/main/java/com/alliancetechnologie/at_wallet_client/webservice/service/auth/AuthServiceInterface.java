package com.alliancetechnologie.at_wallet_client.webservice.service.auth;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;

public interface AuthServiceInterface {

    void onSuccessLogin(User user);

    void onSuccessRegistration(ResponseVerification responseVerification);

    void onCompleteVerifyRegistration();

    void onSuccessForgetPassword(ResponseVerification responseVerification);

    void onSuccessResendCode(ResponseVerification responseVerification);

    void onCompleteRestPassword();

    void onError(String msg, int code);
}
