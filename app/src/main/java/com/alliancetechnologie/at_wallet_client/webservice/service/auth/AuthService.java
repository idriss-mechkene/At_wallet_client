package com.alliancetechnologie.at_wallet_client.webservice.service.auth;

import com.alliancetechnologie.at_wallet_client.entity.User;
import com.alliancetechnologie.at_wallet_client.payload.RequestInscription;
import com.alliancetechnologie.at_wallet_client.payload.RequestLogin;
import com.alliancetechnologie.at_wallet_client.payload.RequestRestPassword;
import com.alliancetechnologie.at_wallet_client.payload.ResponseVerification;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.Urls;
import com.alliancetechnologie.at_wallet_client.webservice.retrofit.WebService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

//getData from api
public class AuthService {

    public Single<User> login(RequestLogin requestLogin) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().login(requestLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseVerification> registration(RequestInscription requestInscription) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().registration(requestInscription)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable verificationRegistration(Long requestNumber, String verificationCode) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().verification_registration(requestNumber, verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseVerification> forgetPassword(String email) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().forget_password(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseVerification> resendCode(Long RequestNumber, String email) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().resend_code(RequestNumber, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable restPassword(RequestRestPassword requestRestPassword) {
        return WebService.getInstance(Urls.LOGIN_URL).getApi().rest_password(requestRestPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
